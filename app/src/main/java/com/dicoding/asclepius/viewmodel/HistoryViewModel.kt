package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.model.History
import com.dicoding.asclepius.data.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyRepository: HistoryRepository) : ViewModel() {
    fun getAllHistory(): LiveData<List<History>> = historyRepository.getAllHistory()

    fun insertHistory(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.insertHistory(history)
        }
    }

    fun deleteAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.deleteAllHistory()
        }
    }
}