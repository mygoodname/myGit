package com.tangbing.admin.kotlindebug.context

import android.app.Activity
import java.util.*

/**
 * Created by tangbing on 2020/1/19.
 * Describe :
 *
 */
class AppManager(){

    companion object{
        val activityStack= Stack<Activity>()
        fun newInstance():AppManager{
            return SingleInstanceHolder.sInstance
        }
        private class SingleInstanceHolder{
          companion object{
              var sInstance=AppManager()
          }
        }
    }
    fun addActivity(activity: Activity){
        activityStack.add(activity)
    }

}