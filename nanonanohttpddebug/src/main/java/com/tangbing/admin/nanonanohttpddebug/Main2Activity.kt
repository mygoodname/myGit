package com.tangbing.admin.nanonanohttpddebug

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main2.view.*

class Main2Activity : AppCompatActivity() {
    lateinit var webView:WebView
    val webViewClient=object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.loadUrl(url)
            return true
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        initView()
    }
    fun showHtml(view: View){
        webView.loadUrl("http://localhost:8888/index.xhtml")
//        webView.loadUrl("file:///android_asset/index.html")
    }
    fun initView(){
        webView=findViewById(R.id.webView)
        webView.webViewClient= webViewClient
    }
}
