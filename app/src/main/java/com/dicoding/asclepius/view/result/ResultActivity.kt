package com.dicoding.asclepius.view.result

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import kotlin.let

@Suppress("DEPRECATION")
class ResultActivity : AppCompatActivity() {
    private lateinit var binding : ActivityResultBinding

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val titleToolbar = binding.textView
        supportActionBar?.title = titleToolbar.text.toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(false) // Nonaktifkan ikon "back" bawaan

        // Set onClickListener untuk ImageView khusus
        binding.imageView.setOnClickListener {
            onBackPressed() // Aksi kembali saat ikon diklik
        }


        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val results = intent.getStringExtra(EXTRA_RESULTS)
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
        }

        binding.resultText.text = results
    }

    companion object {
        const val EXTRA_RESULTS = "extra_result"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}