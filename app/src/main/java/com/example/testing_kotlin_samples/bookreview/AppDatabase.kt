package com.example.testing_kotlin_samples.bookreview

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testing_kotlin_samples.bookreview.Dao.HistoryDao
import com.example.testing_kotlin_samples.bookreview.Dao.ReviewDao
import com.example.testing_kotlin_samples.bookreview.model.Historybook
import com.example.testing_kotlin_samples.bookreview.model.Review

@Database(entities = [Historybook::class, Review::class],version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun reviewDao(): ReviewDao
}