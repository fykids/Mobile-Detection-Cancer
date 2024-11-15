package com.dicoding.asclepius.view.fragment.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.FragmentHomeBinding
import com.dicoding.asclepius.helper.DateHelper
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.local.database.History
import com.dicoding.asclepius.view.fragment.history.HistoryViewModelFactory
import com.dicoding.asclepius.view.result.ResultActivity
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {

    private var currentImageUri: Uri? = null

    private lateinit var classifyStaticImage: ImageClassifierHelper

    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    private var history: History? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = obtainViewModel(this@HomeFragment)

        // Observasi perubahan URI gambar
        homeViewModel.currentImageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                currentImageUri = it
                showImage(it)
            }
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                Log.d("AnalyzeButton", "Selected image URI: $it") // Log URI yang dipilih
                analyzeImage()
            } ?: showToast(getString(R.string.pilih_gambarnya_dulu_ya))
        }
    }

    private fun obtainViewModel(fragment: Fragment): HomeViewModel {
        val factory = HistoryViewModelFactory.getInstance(fragment.requireActivity().application)
        return ViewModelProvider(fragment, factory)[HomeViewModel::class.java]
    }

    private fun startGallery() {
        // Memulai memilih gambar dari gallery
        launcherIntentGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherIntentGallery =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri != null) {
                Log.d("Gallery", "Image selected: $uri") // Log gambar yang dipilih
                homeViewModel.setCurrentImageUri(uri)
            } else {
                Log.d("Gallery", "No image selected")
            }
        }

    private fun showImage(uri: Uri) {
        Log.d("Image URI", "showImage: $uri") // Log URI gambar yang ditampilkan
        binding.previewImageView.setImageURI(uri)
    }

    private fun analyzeImage() {
        // Pastikan URI gambar tidak null sebelum melakukan klasifikasi
        currentImageUri?.let { uri ->
            binding.progressIndicator.visibility = View.VISIBLE
            classifyStaticImage = ImageClassifierHelper(
                context = requireContext(),
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        requireActivity().runOnUiThread {
                            binding.progressIndicator.visibility = View.GONE
                            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onResults(result: List<Classifications>?, interfaceTime: Long) {
                        binding.progressIndicator.visibility = View.GONE
                        result?.let {
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                val sortedCategories =
                                    it[0].categories.sortedByDescending { it?.score }
                                val displayResult =
                                    sortedCategories.joinToString("\n") {
                                        "${it.label} " + NumberFormat.getPercentInstance()
                                            .format(it.score).trim()
                                    }
                                Log.d("AnalyzeResult", "Classification result: $displayResult")
                                moveToResult(displayResult)
                            } else {
                                showToast(getString(R.string.pilih_gambarnya_dulu_ya))
                            }
                        }
                    }
                }
            )
            // Lakukan klasifikasi gambar
            classifyStaticImage.classifyStaticImage(uri)
        } ?: showToast(getString(R.string.pilih_gambarnya_dulu_ya))
    }

    private fun moveToResult(displayResult: String) {
        Log.d("ResultActivity", "Moving to result with: $displayResult")
        history = History(
            results = displayResult,
            imageClassifier = currentImageUri.toString(),
            date = DateHelper.getCurrentDate()
        )

        homeViewModel.insert(history as History)

        val intent = Intent(requireActivity(), ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_RESULTS, displayResult)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri?.toString())
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_HISTORY = "extra_history"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
