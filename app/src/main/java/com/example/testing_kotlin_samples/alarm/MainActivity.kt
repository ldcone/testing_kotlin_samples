package com.example.testing_kotlin_samples.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.testing_kotlin_samples.R
import java.util.*

class MainActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_alarm)

        initOnOffButton()
        initChangAlarmTimeButton()

        val model = fetchDataFromSharedPreferences()
        renderView(model)

        //step1 데이터 가져오기


        // step2 뷰에 데이터를 그려주기

    }




    private fun initChangAlarmTimeButton() {
        val changeAlarmButton = findViewById<Button>(R.id.changeAlarmTimerBtn)
        changeAlarmButton.setOnClickListener {
            //현재시간 가져오기
            val calendar = Calendar.getInstance()
            TimePickerDialog(this,{ picker,hour,minute ->

                val model = saveAlarmModel(hour,minute,false)
                renderView(model)
                cancelAlarm()


            },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false)
                .show()

        }

    }

    private fun initOnOffButton() {
        val onOffButton = findViewById<Button>(R.id.onOffBtn)
        onOffButton.setOnClickListener {
            Log.d("btn_clicked","clicked")
            Log.d("tag","${it.tag}")
            var model = it.tag as? AlarmDisplayModel ?: return@setOnClickListener
            Log.d("tag","${model}")
            val newModel = saveAlarmModel(model.hour,model.minute,model.onOff.not())//not() 값 반전
            renderView(newModel)
            Log.d("newModel","$newModel")
            if(newModel.onOff){
                //꺼진 경우 등록
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY,newModel.hour)
                    set(Calendar.MINUTE,newModel.minute)

                    //
                    if(before(Calendar.getInstance())){
                        add(Calendar.DATE,1)
                    }
                }

                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this,AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
                intent,PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent)

            }else{
                cancelAlarm()
                // 꺼진경우 제거
            }
            //데이터 확인

            //온온프에 따라 작업을 처리
        }
    }

    private fun saveAlarmModel(
        hour:Int,
        minute:Int,
        onOff:Boolean
    ):AlarmDisplayModel{
        val model = AlarmDisplayModel(
            hour =hour,
            minute=minute,
            onOff = onOff
        )
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()){
            putString(ALARM_KEY,model.makeDataForDB())
            putBoolean(ONOFF_KEY,model.onOff)
            commit()
        }
        return model
    }

    private fun renderView(model: AlarmDisplayModel) {
        findViewById<TextView>(R.id.ampmTV).apply {
            text = model.ampmText
        }
        findViewById<TextView>(R.id.timeTV).apply {
            text = model.timeText
        }

        findViewById<Button>(R.id.onOffBtn).apply {
            text = model.onOffText
            tag = model
        }

    }

    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val timeDBvalue = sharedPreferences.getString(ALARM_KEY,"9:30") ?: "9:30"
        val onOffDBValue = sharedPreferences.getBoolean(ONOFF_KEY,false)
        val alarmData = timeDBvalue.split(":")

        val alarmModel = AlarmDisplayModel(
            hour = alarmData[0].toInt(),
            minute = alarmData[1].toInt(),
            onOff = onOffDBValue
        )

        //보정 예외처리
        val pendingIntent = PendingIntent.getBroadcast(this,
            ALARM_REQUEST_CODE,
            Intent(this,AlarmReceiver::class.java),
        PendingIntent.FLAG_NO_CREATE)

        if((pendingIntent == null) and alarmModel.onOff){
            //알람은 꺼져있는데, 데이터는 켜져있는 경우
            alarmModel.onOff = false
        }else if((pendingIntent != null) and alarmModel.onOff.not()){
            //알람은 켜져있는데, 데이터는 꺼져있는 경우
            //알람을 취소함
            pendingIntent.cancel()
        }
        return alarmModel
    }

    private fun cancelAlarm(){
        val pendingIntent = PendingIntent.getBroadcast(this,
            ALARM_REQUEST_CODE,
            Intent(this,AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE)
        pendingIntent?.cancel()
    }

    companion object{
        private const val ALARM_KEY = "alarm"
        private const val ONOFF_KEY = "onOff"
        private const val SHARED_PREFERENCES_NAME = "time"
        private const val ALARM_REQUEST_CODE = 1000
    }
}













