package com.tangbing.admin.nanonanohttpddebug

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity() {
    var mHttpServer:MyWebServer?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun startService(view:View){


        var intent2=Intent(this,Main2Activity().javaClass)
        startActivity(intent2)
    }

    override fun onResume() {
        super.onResume()
       /* mHttpServer = MyWebServer(this)
        mHttpServer?.start()*/
        var intent=Intent(this,MyService().javaClass)
         startService(intent)
    }
}