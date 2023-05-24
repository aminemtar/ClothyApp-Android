package com.example.clothy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.clothy.R

class TermsandConditions : AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_termsand_conditions)
        webView=findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.visibility= View.VISIBLE
        webView.loadUrl("http://192.168.115.113:9090/term/")
    }
    fun goBack(view: View) {
        finish()
    }
}