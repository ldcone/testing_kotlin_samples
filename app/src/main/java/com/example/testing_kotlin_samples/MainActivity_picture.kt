package com.example.testing_kotlin_samples

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity_picture:AppCompatActivity() {
    private val addPhotoButton: Button by lazy{
        findViewById(R.id.addPhotoButton)
    }
    private val startPhotoFrameModeButton: Button by lazy{
        findViewById(R.id.startPhotoFrameModeButton)
    }

    private val imageViewList: List<ImageView> by lazy{
        mutableListOf<ImageView>().apply {
            add(findViewById(R.id.imageview11))
            add(findViewById(R.id.imageview12))
            add(findViewById(R.id.imageview13))
            add(findViewById(R.id.imageview21))
            add(findViewById(R.id.imageview22))
            add(findViewById(R.id.imageview23))
        }
    }

    private  val imageUriList: MutableList<Uri> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_picture)

        initAddPhotoButton()
        initStartPhotoFrameModeButton()
    }

    private fun initAddPhotoButton() {
        addPhotoButton.setOnClickListener {
            when{
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )== PackageManager.PERMISSION_GRANTED ->{
                    //todo 권한이 잘 부여되었을때 갤러리에서 사진을 선택하는 기능
                    nevigatePhotos()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)->{

                    showPermissionContextPopup()
                }
                else ->{
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1000->{
                if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    nevigatePhotos()
                }else{
                    Toast.makeText(this,"권한을 거부 하셨습니다.",Toast.LENGTH_SHORT).show()
                }
            }
            else -> {

            }
        }
    }

    private fun nevigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type ="image/*"
        startActivityForResult(intent,2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK){
            return
        }

        when(requestCode){
            2000 -> {
                val selectedImageUrl: Uri? = data?.data
                if(selectedImageUrl != null){
                    if(imageUriList.size == 6){
                        Toast.makeText(this,"이미 사진이 꽉찼습니다.",Toast.LENGTH_SHORT).show()
                        return
                    }
                    imageUriList.add(selectedImageUrl)
                    imageViewList[imageUriList.size -1].setImageURI(selectedImageUrl)
                }else{
                    Toast.makeText(this,"사진을 가져오지 못했습니다.",Toast.LENGTH_SHORT).show()
                }
            }
            else->{
                Toast.makeText(this,"사진을 가져오지 못했습니다",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("전자액자에 앱에서 사진을 불러오기 위해 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }

    private fun initStartPhotoFrameModeButton() {
        startPhotoFrameModeButton.setOnClickListener {
            if(imageUriList.size !=0){
                val intent = Intent(this, PhotoFrameActivity::class.java)
                imageUriList.forEachIndexed{index, uri->
                    intent.putExtra("photo$index",uri.toString())
                }
                intent.putExtra("photoListSize", imageUriList.size)
                startActivity(intent)
            }else{
                Toast.makeText(this,"사진이 없습니다.",Toast.LENGTH_SHORT).show()
            }

        }
    }


}