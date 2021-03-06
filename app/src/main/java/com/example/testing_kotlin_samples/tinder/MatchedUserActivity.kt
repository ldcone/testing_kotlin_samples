package com.example.testing_kotlin_samples.tinder

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testing_kotlin_samples.R
import com.example.testing_kotlin_samples.tinder.DBKey.Companion.LIKED_BY
import com.example.testing_kotlin_samples.tinder.DBKey.Companion.NAME
import com.example.testing_kotlin_samples.tinder.DBKey.Companion.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MatchedUserActivity:AppCompatActivity() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userDB: DatabaseReference
    private val adapter = MatchedUserAdapter()
    private val carditem = mutableListOf<Carditem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tinder_match)
        userDB = Firebase.database.reference.child(USERS)
        initMatchedUserRecyclerView()
        getMatchUsers()


    }

    private fun getMatchUsers() {
        val matchedDB = userDB.child(getCurrentUserID()).child(LIKED_BY).child("match")
        matchedDB.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.key?.isNotEmpty() == true){
                    getUserByKey(snapshot.key.orEmpty())
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun getUserByKey(userId: String) {
        userDB.child(userId).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                carditem.add(Carditem(userId,snapshot.child(NAME).value.toString()))
                adapter.submitList(carditem)
            }

            override fun onCancelled(error: DatabaseError) {}

        })

    }

    private fun initMatchedUserRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.matchedRecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun getCurrentUserID():String{
        if(auth.currentUser == null){
            Toast.makeText(this,"???????????? ???????????? ????????????. ", Toast.LENGTH_SHORT).show()
            finish()
        }
        return auth.currentUser?.uid.orEmpty()
    }
}