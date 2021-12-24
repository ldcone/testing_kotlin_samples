package com.example.testing_kotlin_samples.tinder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testing_kotlin_samples.R
import com.example.testing_kotlin_samples.databinding.ActivityTinderMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity:AppCompatActivity() {
    private lateinit var binding:ActivityTinderMainBinding
    private val auth:FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTinderMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.logoutbutton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onStart() {
        super.onStart()

        if(auth.currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
        }else{
            startActivity(Intent(this,LikeActivity::class.java))
            finish()
        }
    }
}