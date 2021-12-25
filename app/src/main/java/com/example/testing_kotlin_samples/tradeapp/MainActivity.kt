package com.example.testing_kotlin_samples.tradeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testing_kotlin_samples.databinding.ActivityTradeMainBinding

class MainActivity:AppCompatActivity() {
    private lateinit var binding:ActivityTradeMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTradeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}