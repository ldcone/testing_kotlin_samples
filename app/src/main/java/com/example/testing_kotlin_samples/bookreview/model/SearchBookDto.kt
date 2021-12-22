package com.example.testing_kotlin_samples.bookreview.model

import com.google.gson.annotations.SerializedName

data class SearchBookDto (
    @SerializedName("title") val title:String,
    @SerializedName("item") val item:List<Book>
    )