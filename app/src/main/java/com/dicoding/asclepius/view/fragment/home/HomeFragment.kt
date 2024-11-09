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

    private var currentImageUri : Uri? = null

    private lateinit var classifyStaticImage : ImageClassifierHelper

    private var _binding : FragmentHomeBinding? = null
    val binding get() = _binding!!

    private lateinit var homeViewModel : HomeViewModel

    private var history : History? = null

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?,
    ) : View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = obtainViewModel(this@HomeFragment)

        history = requireActivity().intent.getParcelableExtra(EXTRA_HISTORY)
        history = History()

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage()
            }
        }
    }

    private fun obtainViewModel(fragment : Fragment) : HomeViewModel {
        val factory = HistoryViewModelFactory.getInstance(fragment.requireActivity().application)
        return ViewModelProvider(fragment, factory)[HomeViewModel::class.java]
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherIntentGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherIntentGallery =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri : Uri? ->
            if (uri != null) {
                currentImageUri = uri
                showImage()
            } else {
                Log.d("Photo Picker", "No Media Selected")
            }
        }

    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        currentImageUri.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        binding.progressIndicator.visibility = View.VISIBLE
        classifyStaticImage = ImageClassifierHelper(
            context = requireContext(),
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error : String) {
                    requireActivity().runOnUiThread {
                        binding.progressIndicator.visibility = View.GONE
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(
                    result : List<Classifications>?,
                    interfaceTime : Long,
                ) {
                    binding.progressIndicator.visibility = View.GONE
                    result?.let { it ->
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            print(it)
                            val sortedCategories =
                                it[0].categories.sortedByDescending { it?.score }
                            val displayResult =
                                sortedCategories.joinToString("\n") {
                                    "${it.label} " + NumberFormat.getPercentInstance()
                                        .format(it.score).trim()
                                }
                            moveToResult(displayResult)
                        } else {
                            binding.progressIndicator.visibility = View.GONE
                            showToast(getString(R.string.pilih_gambarnya_dulu_ya))
                        }
                    }
                }
            }
        )
        currentImageUri?.let {
            classifyStaticImage.classifyStaticImage(it)
        }
    }

    private fun moveToResult(displayResult : String) {

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

    private fun showToast(message : String) {
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