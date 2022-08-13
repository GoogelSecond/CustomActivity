package com.example.customactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var clockView: ClockView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clockView = findViewById(R.id.clock_view)

        val handlerThread = HandlerThread("")
        handlerThread.start()

        val handler = Handler(handlerThread.looper)

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