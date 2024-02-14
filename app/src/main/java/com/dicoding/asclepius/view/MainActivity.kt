package com.dicoding.asclepius.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.RoundedCornersTransformation
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.model.History
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.viewmodel.HistoryViewModel
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private var currentImageUri: Uri? = null

    private val historyViewModel: HistoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage(it)
            } ?: run { showToast(getString(R.string.insert_image)) }
        }
    }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                startUCropActivity(uri)
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }

    private val cropLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val resultUri = UCrop.getOutput(data)
                    if (resultUri != null) {
                        showImage(resultUri)
                        currentImageUri = resultUri
                    } else {
                        showToast(getString(R.string.ucrop_null))
                    }
                } else {
                    showToast(getString(R.string.intent_data_null))
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                showImage(currentImageUri)
            } else if (result.resultCode == UCrop.RESULT_ERROR) {
                showToast(getString(R.string.error_cropping_image))
            }
        }

    private fun startUCropActivity(uri: Uri) {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val outputUri = Uri.fromFile(File(cacheDir, "cropped_image_$timeStamp"))
        val uCrop = UCrop.of(uri, outputUri)
            .withAspectRatio(4f, 3f)
        cropLauncher.launch(uCrop.getIntent(this@MainActivity))
    }

    private fun startGallery() {
        launcherGallery.launch("image/*")
    }

    private fun showImage(imageUri: Uri?) {
        imageUri?.let {
            binding.previewImageView.load(it) {

                crossfade(true)
                transformations(RoundedCornersTransformation(12f))
            }
        }
    }

    private fun analyzeImage(imageUri: Uri) {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(result: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        result?.let { it ->
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                val sortedCategories =
                                    it[0].categories.sortedByDescending { it?.score }
                                val results = "${sortedCategories[0].label} ${
                                    NumberFormat.getPercentInstance()
                                        .format(sortedCategories[0].score).trim()
                                }"
                                lifecycleScope.launch {
                                    val history = History(
                                        image = currentImageUri.toString(),
                                        result = sortedCategories[0].label,
                                        score = NumberFormat.getPercentInstance()
                                            .format(sortedCategories[0].score).trim()
                                    )
                                    historyViewModel.insertHistory(history)
                                }
                                moveToResult(results)
                            } else {
                                showToast(getString(R.string.somwthing_wrong))
                            }
                        }
                    }
                }
            }
        )
        imageClassifierHelper.classifyStaticImage(imageUri)
    }

    private fun moveToResult(results: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.apply {
            putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
            putExtra(ResultActivity.EXTRA_RESULT, results)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.history) {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}