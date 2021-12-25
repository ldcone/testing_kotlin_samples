package com.example.testing_kotlin_samples.tradeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.testing_kotlin_samples.R
import com.example.testing_kotlin_samples.databinding.ActivityTradeMainBinding
import com.example.testing_kotlin_samples.tradeapp.chatlist.ChatListFragment
import com.example.testing_kotlin_samples.tradeapp.home.HomeFragment
import com.example.testing_kotlin_samples.tradeapp.mypage.MyPageFragment

class MainActivity:AppCompatActivity() {
    private lateinit var binding:ActivityTradeMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTradeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val chatListFragment = ChatListFragment()
        val myPageFragment = MyPageFragment()

        replaceFragment(homeFragment)

        binding.BottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home-> replaceFragment(homeFragment)
                R.id.chatList -> replaceFragment(chatListFragment)
                R.id.myPage -> replaceFragment(myPageFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer,fragment)
                commit()
            }

    }
}