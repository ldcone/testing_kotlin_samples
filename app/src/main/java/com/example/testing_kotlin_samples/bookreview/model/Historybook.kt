package com.example.testing_kotlin_samples.bookreview.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Historybook (
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name="keyword")val keyword: String?
    )