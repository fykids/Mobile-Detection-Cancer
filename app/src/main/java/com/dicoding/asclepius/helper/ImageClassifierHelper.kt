package com.dicoding.asclepius.helper

import com.dicoding.asclepius.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.util.Log
import org.checkerframework.checker.units.qual.Time
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier


class ImageClassifierHelper(
    var threshold : Float = 0.1f,
    var maxResults: Int = 1,
    val modelName: String = "cancer_classification.tflite",
    val context : Context,
    val classifierListener: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            result : List<Classifications>?,
            interfaceTime : Long
        )
    }

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        // TODO: Menyiapkan Image Classifier untuk memproses gambar.
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)

        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        // TODO: mengklasifikasikan imageUri dari gambar statis.
        try {
            val imageStream = context.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(imageStream)
            val tensorImage = TensorImage.fromBitmap(bitmap)

            val results = imageClassifier?.classify(tensorImage)
            classifierListener?.onResults(results, System.currentTimeMillis())
        } catch (exc: Exception) {
            classifierListener?.onError("Failed to Classify Image: ${exc.message}")
        }

    }

    companion object {
        const val TAG = "ImageClassifierHelper"
    }

}