package com.example.testing_kotlin_samples.tinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.testing_kotlin_samples.databinding.ActivityTinderLoginBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity:AppCompatActivity() {

    private lateinit var binding : ActivityTinderLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager:CallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTinderLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        callbackManager = CallbackManager.Factory.create()

        initLoginButton()
        initSignUpButton()
        initEamilAndPasswordEditText()
        initFacebookLoginButton()


    }

    private fun initFacebookLoginButton() {
        binding.facebookLoginBtn.setPermissions("email","public_profile")
        binding.facebookLoginBtn.registerCallback(callbackManager,object : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult) {
                //로그인 성공적
                val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this@LoginActivity) {task->
                        if(task.isSuccessful){
                            successLogin()
                        }else{
                            Toast.makeText(this@LoginActivity,"페이스북 로그인 실패",Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException?) {
            Toast.makeText(this@LoginActivity,"페이스북 로그인 실패",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun initSignUpButton(){
        //회원가입 버튼을 통한 파이어베이스 회원가입 시도
        binding.signUpButton.setOnClickListener {
            val email = getInputEmail()
            val password = getInputpassword()

            Log.d("LoginActivity","email= $email, password = $password")

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {task ->
                    if(task.isComplete){
                        Log.d("LoginActivity","complete")
                    }
                    if(task.isSuccessful){
                        Toast.makeText(this,"회원가입 성공! 로그인을 진행해 주세요.",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,"회원가입 실패!",Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }


    private fun initLoginButton(){
        //파이어베이스를 통해 로그인 시도
        binding.loginButton.setOnClickListener {
            val email = getInputEmail()
            val password = getInputpassword()

            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful){
                        successLogin()
                    }else{
                        Toast.makeText(this,"로그인 실패 ",Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }
    private fun initEamilAndPasswordEditText(){
        // 이메일과 비빌번호를 넘길수 있을때 버튼 활성화
        binding.emailET.addTextChangedListener{
            val enable = binding.emailET.text.isNotEmpty() && binding.passwordET.text.isNotEmpty()
            binding.loginButton.isEnabled = enable
            binding.signUpButton.isEnabled = enable
        }
        binding.passwordET.addTextChangedListener{
            val enable = binding.emailET.text.isNotEmpty() && binding.passwordET.text.isNotEmpty()
            binding.loginButton.isEnabled = enable
            binding.signUpButton.isEnabled = enable
        }
    }

    private fun getInputEmail():String {
        //edittext에서 이메일 넘기기
        return binding.emailET.text.toString()

    }

    private fun getInputpassword():String {
        //edittext에서 패스워드 넘기기
        return binding.passwordET.text.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(resultCode,resultCode,data)
    }

    private fun successLogin(){
        if(auth.currentUser == null){
            Toast.makeText(this,"로그인에 실패했습니다.",Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid.orEmpty()
        val currentUserDB = Firebase.database.reference.child("Users").child(userId)
        val user = mutableMapOf<String,Any>()
        user["userId"] = userId
        currentUserDB.updateChildren(user)
        finish()
    }


}