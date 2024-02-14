package com.dicoding.asclepius.data.repository

import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.database.HistoryDao
import com.dicoding.asclepius.data.model.History

class HistoryRepository(private val historyDao: HistoryDao) {
    fun getAllHistory(): LiveData<List<History>> = historyDao.getAllHistory()
    suspend fun insertHistory(history: History) = historyDao.insert(history)
    suspend fun deleteAllHistory() = historyDao.deleteAllHistory()
}