package com.dicoding.asclepius.data.model

data class Article(
    val articles: List<ArticleData>
)

data class ArticleData(
    val title: String,
    val url: String,
    val urlToImage: String,
)
