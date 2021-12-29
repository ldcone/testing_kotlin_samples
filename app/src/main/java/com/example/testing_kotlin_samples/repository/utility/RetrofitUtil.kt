package com.example.testing_kotlin_samples.repository.utility

import com.example.testing_kotlin_samples.BuildConfig
import com.example.testing_kotlin_samples.repository.data.response.Url.GITHUB_URL
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

object RetrofitUtil {
    val authApiService:AuthApiService by lazy { getGithubAuthRetrofit().create(AuthApiService::class.java) }

    private fun getGithubAuthRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl(GITHUB_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()
                )
            )
            .client(buildOkHttpClient())
            .build()
    }

    private fun buildOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        if(BuildConfig.DEBUG){
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        }else{
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .connectTimeout(5,TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()

    }


}