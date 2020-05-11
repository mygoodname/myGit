package com.tangbing.admin.kotlindebug.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tangbing.admin.kotlindebug.R
import com.tangbing.admin.kotlindebug.databinding.ActivityContentBinding

/**
 * Created by tangbing on 2020/5/7.
 * Describe :
 *
 */
class ContentActivity : AppCompatActivity() {
    lateinit var contentBinding: ActivityContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentBinding =DataBindingUtil.setContentView<ActivityContentBinding>(this, R.layout.activity_content)
        contentBinding.texthaha.text="djalkjkladjfkldalk";
        contentBinding.texthaha.setBackgroundColor(resources.getColor(R.color.colorPrimary))
    }

}