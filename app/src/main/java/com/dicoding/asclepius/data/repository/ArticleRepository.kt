package com.dicoding.asclepius.data.repository

import com.dicoding.asclepius.data.api.ApiClient
import com.dicoding.asclepius.data.model.ArticleData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ArticleRepository(private val apiClient: ApiClient) {
    fun getArticles(): Flow<List<ArticleData>> {
        return flow {
            val response = apiClient.getArticles()
            if (response.isSuccessful) {
                response.body()
                    ?.let { emit(it.articles.filter { article -> article.title != "[Removed]" }) }
            } else {
                throw Exception("Error: ${response.code()}")
            }
        }
    }
}