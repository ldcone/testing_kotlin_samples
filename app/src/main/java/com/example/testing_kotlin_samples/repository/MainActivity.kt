package com.example.testing_kotlin_samples.repository

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isGone
import com.example.testing_kotlin_samples.BuildConfig
import com.example.testing_kotlin_samples.databinding.ActivityRepositoryMainBinding
import com.example.testing_kotlin_samples.repository.utility.AuthTokenProvider
import com.example.testing_kotlin_samples.repository.utility.RetrofitUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity:AppCompatActivity(),CoroutineScope {
    private lateinit var binding:ActivityRepositoryMainBinding

    private val authTokenProvider by lazy { AuthTokenProvider(this) }

    val job:Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepositoryMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }
    private fun initViews() = with(binding){
        loginButton.setOnClickListener {
            loginGithub()
        }
    }

    // todo https://github.com/login/oauth/authorize?clinet_id=
    private fun loginGithub() {
        val loginUri = Uri.Builder().scheme("https").authority("github.com")
            .appendPath("login")
            .appendPath("oauth")
            .appendPath("authorize")
            .appendQueryParameter("client_id",BuildConfig.GITHUB_CLIENT_ID)
            .build()

        CustomTabsIntent.Builder().build().also {
            it.launchUrl(this,loginUri)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.data?.getQueryParameter("code")?.let{
            //todo getAccessToken
            launch(coroutineContext) {
                showProgress()
                val getAccessTokenJob = getAccessToken(it)
                dismissProgress()
            }
        }
    }

    private suspend fun showProgress() = withContext(coroutineContext){
        with(binding){
            loginButton.isGone = true
            progressBar.isGone = false
            progressTV.isGone = false
        }
    }

    private suspend fun dismissProgress() = withContext(coroutineContext){
        with(binding){
            loginButton.isGone = false
            progressBar.isGone = true
            progressTV.isGone = true
        }
    }

    private suspend fun getAccessToken(code:String) = withContext(Dispatchers.IO){
        val response = RetrofitUtil.authApiService.getAccessToken(
            clientId = BuildConfig.GITHUB_CLIENT_ID,
            clientSecret = BuildConfig.GITHUB_CLIENT_SECRET,
            code = code
        )
        if(response.isSuccessful){
            val accessToken = response.body()?.accessToken ?: ""
            Log.e("accessToken", accessToken)
            if(accessToken.isNotEmpty()){
                authTokenProvider.updateToken(accessToken)
            }else{
                Toast.makeText(this@MainActivity,"no accessToken",Toast.LENGTH_SHORT).show()
            }


        }
    }









}