package com.dicoding.asclepius.view.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.model.ArticleData
import com.dicoding.asclepius.databinding.ItemArticleBinding

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private val articles = ArrayList<ArticleData>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<ArticleData>) {
        articles.clear()
        articles.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    class ViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleData) {
            binding.apply {
                articleImage.load(article.urlToImage) {
                    placeholder(R.drawable.ic_place_holder)
                    error(R.drawable.ic_place_holder)
                    crossfade(true)
                    transformations(RoundedCornersTransformation(12f))
                }
                title.text = article.title
            }

            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                it.context.startActivity(intent)
            }
        }
    }
}