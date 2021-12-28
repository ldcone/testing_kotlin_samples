package com.example.testing_kotlin_samples.tradeapp.chatlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testing_kotlin_samples.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class chatRoomActivity:AppCompatActivity() {
    private val auth:FirebaseAuth by lazy {
        Firebase.auth
    }
    private val chatList = mutableListOf<ChatItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade_chatroom)
    }
}