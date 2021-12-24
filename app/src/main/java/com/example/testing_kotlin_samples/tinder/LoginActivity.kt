package com.example.testing_kotlin_samples.tinder

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.testing_kotlin_samples.databinding.ActivityTinderLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity:AppCompatActivity() {
    private lateinit var binding : ActivityTinderLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTinderLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        initLoginButton()
        initSignUpButton()
        initEamilAndPasswordEditText()

    }

    private fun initSignUpButton(){
        binding.signUpButton.setOnClickListener {
            val email = getInputEmail()
            val password = getInputpassword()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {task ->
                    if(task.isSuccessful){
                        Toast.makeText(this,"회원가입 성공! 로그인을 진행해 주세요.",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,"회원가입 실패!",Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }


    private fun initLoginButton(){
        binding.loginButton.setOnClickListener {
            val email = getInputEmail()
            val password = getInputpassword()

            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful){
                        finish()
                    }else{
                        Toast.makeText(this,"로그인 실패 ",Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }
    private fun initEamilAndPasswordEditText(){
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
        return binding.emailET.text.toString()

    }

    private fun getInputpassword():String {
        return binding.passwordET.text.toString()
    }


}