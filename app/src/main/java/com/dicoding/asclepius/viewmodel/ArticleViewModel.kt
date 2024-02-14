package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.model.ArticleData
import com.dicoding.asclepius.data.repository.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class ArticleViewModel(private val articleRepository: ArticleRepository) : ViewModel() {
    private val _articles: MutableLiveData<List<ArticleData>> = MutableLiveData()
    val articles: LiveData<List<ArticleData>> = _articles

    fun setupData() {
        viewModelScope.launch {
            articleRepository.getArticles().flowOn(Dispatchers.IO).collect {
                _articles.postValue(it)
            }
        }
    }
}