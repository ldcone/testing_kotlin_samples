package com.example.testing_kotlin_samples

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testing_kotlin_samples.dao.HistoryDao
import com.example.testing_kotlin_samples.model.History

@Database(entities = [History::class], version =1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
}