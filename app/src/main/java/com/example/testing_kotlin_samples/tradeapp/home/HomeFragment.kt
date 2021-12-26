package com.example.testing_kotlin_samples.tradeapp.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testing_kotlin_samples.R
import com.example.testing_kotlin_samples.databinding.FragmentHomeBinding

class HomeFragment:Fragment(R.layout.fragment_home) {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var articleAdapter:ArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        articleAdapter = ArticleAdapter()
        articleAdapter.submitList(mutableListOf<ArticleModel>().apply {
            add(ArticleModel("0","지정원",100000,"100000000000000원",""))
            add(ArticleModel("1","이동천",200000,"100000000000000원",""))
        })
        binding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.articleRecyclerView.adapter = articleAdapter


    }
}