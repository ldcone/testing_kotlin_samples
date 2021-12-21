package com.example.testing_kotlin_samples.gettingpush

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.testing_kotlin_samples.R
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity:AppCompatActivity() {

    private val resultTextView:TextView by lazy {
        findViewById(R.id.resultTV)
    }

    private val firebaseToken:TextView by lazy {
        findViewById(R.id.firebaseTokenTV)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actvity_main_gettingpush)

        initFirebase()



    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    firebaseToken.text = task.result
                }
            }
    }
}