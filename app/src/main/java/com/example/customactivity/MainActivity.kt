package com.example.customactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var clockView: ClockView
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handlerThread = HandlerThread("")
        handlerThread.start()

        handler = Handler(handlerThread.looper)

//        val currentTime = TimeHelper.getCurrentTime()

        clockView = findViewById(R.id.clock_view)

//        clockView.timeModel = TimeModel(6f, 58f, 15f)

//        Log.e("MainActivity", "$currentTime")


        val runnable = Runnable {
            run {
                while (true) {
                    clockView.timeModel = TimeHelper.getCurrentTime()
                }
            }
        }


        handler.post(runnable)
    }
}