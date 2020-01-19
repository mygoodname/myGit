package com.tangbing.admin.kotlindebug

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log;

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        d()
    }
    /**
     *包装log.d日志
     */
    fun d() {
        Log.e("MainActivity", ("1".toInt()==1).toString());
    }
}
