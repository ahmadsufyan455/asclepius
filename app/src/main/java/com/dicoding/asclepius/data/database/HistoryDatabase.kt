package com.dicoding.asclepius.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.model.History

@Database(entities = [History::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}