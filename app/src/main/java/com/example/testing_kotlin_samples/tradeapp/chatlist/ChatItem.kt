package com.example.testing_kotlin_samples.tradeapp.chatlist

data class ChatItem(
    val senderId:String,
    val message: String,
){
    constructor():this("","")

}
