package com.dicoding.asclepius.data.api

import com.dicoding.asclepius.constant.Constants
import com.dicoding.asclepius.data.model.Article
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {
    @GET("top-headlines")
    suspend fun getArticles(
        @Query("apiKey") apiKey: String = Constants.apiKey,
        @Query("q") q: String = "cancer",
        @Query("language") language: String = "en",
        @Query("category") category: String = "health"
    ): Response<Article>
}