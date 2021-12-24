package com.example.testing_kotlin_samples.bookreview.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testing_kotlin_samples.bookreview.model.Review

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review WHERE id =:id")
    fun getOneReview(id: Int): Review?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review: Review)
}