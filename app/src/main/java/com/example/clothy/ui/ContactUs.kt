package com.example.clothy.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.clothy.R

class ContactUs : AppCompatActivity() {
    private lateinit var email:TextView
    private lateinit var phone:TextView
    private lateinit var image:ImageView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        email=findViewById(R.id.txtEmail)
        phone = findViewById(R.id.txtPhone)
        image=findViewById(R.id.imgVoucher2)
         email.text="clothy.app@gmail.com"
        phone.text="22826286/52693684"

    }
    fun goBack(view: View) {
        finish()
    }
}