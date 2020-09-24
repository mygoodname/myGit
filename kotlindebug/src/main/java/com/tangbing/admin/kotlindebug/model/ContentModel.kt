package com.tangbing.admin.kotlindebug.model

import android.util.Log
import java.util.ArrayList

/**
 * Created by tangbing on 2020/5/7.
 * Describe :
 *
 */
class ContentModel {
    var a="一条龙服务"
    var b:Int=0
    fun ta(a:String,b:String)=a+b
    fun sum(a: Int, b: Int) = a + b
    fun tb(): Long {
        return 100L
    }
    fun tc() = 100L

    fun te(vararg x:Int){
        for(vt in x){
            Log.d("MainActivity", vt.toString())
        }
    }
    fun tfor(a:ArrayList<String>){
       for (index in a.indices){
           Log.d("MainActivity", a[index])
       }
    }
    fun twhile(){
        var index=0
        while (true){
            if (index<100){
                if (index==50)
                    continue
                Log.d("MainActivity", index.toString())
            }else
                break
            index++
        }
    }
}