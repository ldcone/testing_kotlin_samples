package com.example.testing_kotlin_samples.tradeapp.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testing_kotlin_samples.R
import com.example.testing_kotlin_samples.databinding.FragmentHomeBinding
import com.example.testing_kotlin_samples.tradeapp.DBKey.Companion.DB_ARTICLES
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment:Fragment(R.layout.fragment_home) {
    private lateinit var articleDB: DatabaseReference
    private lateinit var binding:FragmentHomeBinding
    private lateinit var articleAdapter:ArticleAdapter

    private val articlList= mutableListOf<ArticleModel>()

    private val listener = object :ChildEventListener{
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

            val articleModel = snapshot.getValue(ArticleModel::class.java)
            articleModel ?: return

            articlList.add(articleModel)
            articleAdapter.submitList(articlList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

        }

        override fun onChildRemoved(snapshot: DataSnapshot) {

        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {

        }

    }

    private val auth: FirebaseAuth by lazy{
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        articleDB = Firebase.database.reference.child(DB_ARTICLES)
        //view 재사용 과정에서 생기는 중복 피함
        articlList.clear()

        articleAdapter = ArticleAdapter()

        binding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.articleRecyclerView.adapter = articleAdapter

        articleDB.addChildEventListener(listener)


    }

    override fun onResume() {
        super.onResume()
        articleAdapter.notifyDataSetChanged()
    }


    override fun onDestroy() {
        super.onDestroy()
        articleDB.removeEventListener(listener)
    }

}