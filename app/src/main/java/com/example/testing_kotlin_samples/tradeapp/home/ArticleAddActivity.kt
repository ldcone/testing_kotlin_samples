package com.example.testing_kotlin_samples.tradeapp.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.testing_kotlin_samples.databinding.ActivityTradeAddarticleBinding
import com.example.testing_kotlin_samples.tradeapp.DBKey.Companion.DB_ARTICLES
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ArticleAddActivity:AppCompatActivity() {
    private lateinit var binding : ActivityTradeAddarticleBinding
    private val auth:FirebaseAuth by lazy {
        Firebase.auth
    }
    private val storage:FirebaseStorage by lazy {
        Firebase.storage
    }

    private val articleDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DB_ARTICLES)
    }

    private var selectedUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTradeAddarticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageAddButton.setOnClickListener {
            when{
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED ->{
                    startContentProvider()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    showPermissionContextPopup()
                }
                else ->{
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1010)
                }
            }

        }

        binding.submitButton.setOnClickListener {
            val title = binding.titleTV.text.toString()
            val price = binding.priceET.text.toString()
            val sellerid = auth.currentUser?.uid.orEmpty()

            showProgress()

            if(selectedUri != null){
                val photoUri = selectedUri ?: return@setOnClickListener
                uploadPhoto(photoUri,
                    successHandler = {Uri ->
                        uploadArticle(sellerid,title,price,Uri)
                    },
                    errorHandler = {
                        Toast.makeText(this,"?????? ???????????? ??????????????????.",Toast.LENGTH_SHORT).show()
                        hideProgress()
                    }

                    )
            }else{
                uploadArticle(sellerid,title,price,"")

            }


        }
    }
    private fun showProgress(){
        binding.progressBar.isVisible = true
    }
    private fun hideProgress(){
        binding.progressBar.isVisible = false
    }

    private fun uploadPhoto(uri: Uri,successHandler: (String) -> Unit, errorHandler: () -> Unit ) {
        val fileName = "${System.currentTimeMillis()}.png"
        storage.reference.child("article/photo").child(fileName)
            .putFile(uri)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    storage.reference.child("article/photo").child(fileName)
                        .downloadUrl
                        .addOnSuccessListener { uri->
                            successHandler(uri.toString())
                        }.addOnFailureListener {
                            errorHandler()
                        }
                }else{
                    errorHandler()
                }
            }

    }
    private fun uploadArticle(sellerId:String, title:String, price:String, imageUrl:String){
        val model = ArticleModel(sellerId,title,System.currentTimeMillis(),"$price ???",imageUrl)
        articleDB.push().setValue(model)
        hideProgress()
        finish()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("????????? ???????????????.")
            .setMessage("????????? ???????????? ?????? ???????????????.")
            .setPositiveButton("??????"){_,_ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1010)
            }
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            1010->
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startContentProvider()
                }else{
                    Toast.makeText(this,"????????? ?????????????????????.",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun startContentProvider(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,2020)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when(requestCode){
            2020->{
                val uri = data?.data
                if (uri != null){
                    binding.photoImageView.setImageURI(uri)
                    selectedUri = uri
                }else{
                    Toast.makeText(this,"????????? ???????????? ???????????????.",Toast.LENGTH_SHORT).show()
                }

            }
            else ->{
                Toast.makeText(this,"????????? ???????????? ???????????????.",Toast.LENGTH_SHORT).show()            }
        }

    }
}
