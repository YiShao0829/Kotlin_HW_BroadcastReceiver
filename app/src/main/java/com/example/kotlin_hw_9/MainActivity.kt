package com.example.kotlin_hw_9

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    //計數器狀態
    private var flag = false

    private lateinit var btn_start:Button
    private lateinit var tv_clock:TextView

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val b = intent.extras?: return
            tv_clock?.text =  String.format("%02d:%02d:%02d",
                b.getInt("H"), b.getInt("M"), b.getInt("S"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_clock=findViewById(R.id.tv_clock)
        btn_start=findViewById(R.id.btn_start)

        //註冊Receiver來接收有『MyMessage』識別字串的廣播
        registerReceiver(receiver, IntentFilter("MyMessage"))
        //取得Service狀態
        flag = MyService.flag
        btn_start.text = if(flag) "暫停" else "開始"

        btn_start.setOnClickListener {
            flag = !flag
            btn_start.text = if (flag) "暫停" else "開始"
            //啟動Service
            startService(Intent(this, MyService::class.java).putExtra("flag", flag))
            Toast.makeText(this, if(flag)"計時開始" else "計時暫停", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //註銷Receiver
        unregisterReceiver(receiver)
    }
}