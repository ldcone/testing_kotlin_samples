package com.example.testing_kotlin_samples.today_word

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.testing_kotlin_samples.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity:AppCompatActivity() {
    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }
    private  val progressBar:ProgressBar by lazy{
        findViewById(R.id.progressBar)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_todayword)

        initViews()
        initData()

    }

    private fun initViews() {
        viewPager.setPageTransformer{page,position ->
            when{
                position.absoluteValue>=1.0f ->{
                    page.alpha =0f
                }
                position == 0f ->{
                    page.alpha =1f
                }
                else->{
                    page.alpha =1f - 2 * position.absoluteValue
                }
            }
        }
    }

    private fun initData() {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            progressBar.visibility= View.GONE
            if(it.isSuccessful){
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                val isNameRevealed = remoteConfig.getBoolean("is_name_reveal")
                displayQutoesPager(quotes,isNameRevealed)

            }
        }

    }

    private fun displayQutoesPager(quotes: List<Quote>, nameRevealed: Boolean) {
        val adpater =QuotesPagerAdapter(
            quotes,
            nameRevealed
        )

        viewPager.adapter = adpater
        viewPager.setCurrentItem(adpater.itemCount / 2, false)

    }

    private fun parseQuotesJson(json: String): List<Quote> {
        val jsonArray = JSONArray(json)
        var jsonList = emptyList<JSONObject>()

        for(index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let {
                jsonList = jsonList + it
            }
        }
        return jsonList.map{
            Quote(it.getString("quote"),it.getString("name"))
        }
    }


}