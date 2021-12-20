package com.example.testing_kotlin_samples.pomodoro

import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.testing_kotlin_samples.R

class MainActivityPPmo:AppCompatActivity() {

    private val remainMinutesTextView: TextView by lazy{
        findViewById(R.id.remainMinutesTextView)
    }
    private val remainSecondTextView: TextView by lazy {
        findViewById(R.id.remainSecondsTextView)
    }

    private var currentCountdownTimer : CountDownTimer? = null

    private val seekBar:SeekBar by lazy {
        findViewById(R.id.seekBar)
    }

    private val soundPool = SoundPool.Builder().build()
    private var tickingsoundId: Int? = null
    private var bellsoundId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ppomo)
        bindViews()
        initsound()
    }

    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()
        soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    private fun bindViews() {
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    if(p2){
                        updateRemainTime(p1*60*1000L)
                    }

                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    stopCountDown()
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    p0 ?: return

                    if(p0.progress ==0){
                        stopCountDown()
                    }else{
                        startCountDown(p0)
                    }


                }

            }
        )
    }

    private fun stopCountDown(){
        currentCountdownTimer?.cancel()
        currentCountdownTimer = null
        soundPool.autoPause()
    }

    private fun initsound(){
        tickingsoundId = soundPool.load(this, R.raw.alarm,1)
        bellsoundId = soundPool.load(this, R.raw.alarm,1)
    }

    private fun createCountDownTimer(initailMills: Long)=
        object :CountDownTimer(initailMills,1000L){
            override fun onTick(p0: Long) {
                updateRemainTime(p0)
                updateSeekBar(p0)
            }

            override fun onFinish() {
                completeCountDown()

            }
        }

    private fun startCountDown(p0:SeekBar){
        currentCountdownTimer= createCountDownTimer(p0.progress *60 * 1000L)

        currentCountdownTimer?.start()

        tickingsoundId?.let { soundId ->

            soundPool.play(soundId,1F,1F,0,-1,1F)
        }
    }

    private fun completeCountDown(){
        updateRemainTime(0)
        updateSeekBar(0)
        soundPool.autoPause()
        bellsoundId?.let { soundId->
            soundPool.play(soundId,1F,1f,0,0,1f)

        }
    }


    private fun updateRemainTime(remainMillis:Long){
        val remainSeconds = remainMillis / 1000

        remainMinutesTextView.text = "%02d'".format(remainSeconds / 60)
        remainSecondTextView.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBar(remainMillis: Long){
        seekBar.progress = (remainMillis / 1000 / 60).toInt()
    }


}














