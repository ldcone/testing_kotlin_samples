package com.example.testing_kotlin_samples.tradeapp.chatlist

data class ChatListItem(
    val buyer: String,
    val sellerId: String,
    val itemTitle:String,
    val key: Long
){
    constructor():this("","","",0)
}
