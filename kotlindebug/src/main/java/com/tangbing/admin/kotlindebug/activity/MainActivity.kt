package com.tangbing.admin.kotlindebug.activity

import android.content.Intent

import android.os.Bundle
import android.util.Log;
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.tangbing.admin.kotlindebug.R
import com.tangbing.admin.kotlindebug.databinding.ActivityMainBinding
import com.tangbing.admin.kotlindebug.model.ContentModel

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        mainBinding?.goDetail?.setOnClickListener(View.OnClickListener {
            var intent=Intent(this,ContentActivity::class.java)
            startActivity(intent)
        })
        Log.d("MainActivity",ContentModel().a)
    }
    /**
     *包装log.d日志
     */
    fun d() {
        Log.e("MainActivity", ("1".toInt()==1).toString());
    }
}
