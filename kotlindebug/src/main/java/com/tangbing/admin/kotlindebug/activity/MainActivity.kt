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
        ContentModel().te(1,2,3,4,5)
        Log.d("MainActivity", ContentModel().sum(1,2).toString())
//        Log.d("MainActivity", ContentModel().te(1,2,3,4,5).toString())
        a()
    }
    /**
     *包装log.d日志
     */
    fun d() {
        Log.e("MainActivity", ("1".toInt()==1).toString());
    }
    fun a(){
        var array:ArrayList<String> =ArrayList()
        array.add("a")
        array.add("b")
        array.add("c")
        array.add("d")
        array.add("e")
        ContentModel().tfor(array)
        ContentModel().twhile()
    }
}
