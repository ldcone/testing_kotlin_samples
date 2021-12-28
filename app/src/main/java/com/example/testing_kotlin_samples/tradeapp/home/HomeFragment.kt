package com.example.testing_kotlin_samples.tradeapp.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testing_kotlin_samples.R
import com.example.testing_kotlin_samples.databinding.FragmentHomeBinding
import com.example.testing_kotlin_samples.tradeapp.DBKey.Companion.CHILD_CHAT
import com.example.testing_kotlin_samples.tradeapp.DBKey.Companion.DB_ARTICLES
import com.example.testing_kotlin_samples.tradeapp.DBKey.Companion.DB_USERS
import com.example.testing_kotlin_samples.tradeapp.chatlist.ChatListItem
import com.google.android.material.snackbar.Snackbar
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
    private lateinit var userDB: DatabaseReference

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
        userDB = Firebase.database.reference.child(DB_USERS)
        //view 재사용 과정에서 생기는 중복 피함
        articlList.clear()

        articleAdapter = ArticleAdapter(onItemClicked = { ArticleModel ->

            if(auth.currentUser != null){
                if(auth.currentUser?.uid != ArticleModel.sellerId ){
                    val chatRoom= ChatListItem(
                        buyer = auth.currentUser!!.uid,
                        sellerId = ArticleModel.sellerId,
                        itemTitle = ArticleModel.title,
                        key = System.currentTimeMillis()

                    )
                    userDB.child(auth.currentUser!!.uid)
                        .child(CHILD_CHAT)
                        .push()
                        .setValue(chatRoom)

                    userDB.child(ArticleModel.sellerId)
                        .child(CHILD_CHAT)
                        .push()
                        .setValue(chatRoom)

                    Snackbar.make(view,"채팅방이 생성되었습니다.",Snackbar.LENGTH_LONG).show()

                }else{
                    //내가 올린 아이템
                    Snackbar.make(view,"내가 올린 아이템 입니다.",Snackbar.LENGTH_LONG).show()

                }
                }else{
                    //로그인을 안한 상태
                    Snackbar.make(view,"로그인 후 사용해주세요",Snackbar.LENGTH_SHORT).show()
                }

        })

        binding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.articleRecyclerView.adapter = articleAdapter

        articleDB.addChildEventListener(listener)
        binding.addFloatingButton.setOnClickListener {
            if(auth.currentUser != null){
                startActivity(Intent(requireContext(),ArticleAddActivity::class.java))
            }else{
                Toast.makeText(requireContext(),"로그인후 사용 가능",Toast.LENGTH_SHORT).show()
            }


        }


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