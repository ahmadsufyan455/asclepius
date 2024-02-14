package com.dicoding.asclepius.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.view.adapter.ArticleAdapter
import com.dicoding.asclepius.viewmodel.ArticleViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val articleViewModel: ArticleViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Result"

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val results = intent.getStringExtra(EXTRA_RESULT)
        imageUri?.let {
            binding.resultImage.load(it) {
                crossfade(true)
                transformations(RoundedCornersTransformation(12f))
            }
        }
        results?.let { binding.resultText.text = it }

        val articleAdapter = ArticleAdapter()
        binding.progressbar.visibility = View.VISIBLE
        articleViewModel.setupData()
        articleViewModel.articles.observe(this) {
            if (it != null) {
                binding.progressbar.visibility = View.GONE
                articleAdapter.setData(it)
            }
        }

        with(binding.rvArticle) {
            layoutManager = LinearLayoutManager(this@ResultActivity)
            setHasFixedSize(true)
            adapter = articleAdapter
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}