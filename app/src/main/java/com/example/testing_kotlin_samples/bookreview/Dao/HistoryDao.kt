package com.example.testing_kotlin_samples.bookreview.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.testing_kotlin_samples.bookreview.model.Historybook


@Dao
interface HistoryDao {


    @Query("SELECT * FROM historybook")
    fun getAll():List<Historybook>

    @Insert
    fun insertHistory(historybook: Historybook)

    @Query("DELETE FROM historybook WHERE keyword == :keyword")
    fun delete(keyword:String)

}