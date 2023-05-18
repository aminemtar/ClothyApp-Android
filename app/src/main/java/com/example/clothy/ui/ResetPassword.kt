package com.example.clothy.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.clothy.Model.MyApplication
import com.example.clothy.Model.RetrofitClient
import com.example.clothy.Model.UserRequest
import com.example.clothy.Model.UserResponse
import com.example.clothy.R
import com.example.clothy.Service.UserService
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPassword : AppCompatActivity() {
    lateinit var oldPassword: TextInputEditText
    lateinit var newPassword: TextInputEditText
    lateinit var confirmnewPassword: TextInputEditText
    lateinit var Done : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        oldPassword =findViewById(R.id.old)
        newPassword=findViewById(R.id.newP)
        confirmnewPassword =findViewById(R.id.CnewP)
        Done =findViewById(R.id.btnSubmit)
        // red color
        Done.setBackgroundColor(Color.GRAY)
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (newPassword.text.toString() == confirmnewPassword.text.toString() && newPassword.text.toString().isNotEmpty()&& confirmnewPassword.text.toString().isNotEmpty()) {
                    Done.isEnabled = true
                    Done.setBackgroundColor(Color.BLACK)
                } else {
                    Done.isEnabled = false
                    Done.setBackgroundColor(Color.GRAY)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }

        newPassword.addTextChangedListener(textWatcher)
        confirmnewPassword.addTextChangedListener(textWatcher)

        Done.setOnClickListener {
            change(newPassword.text.toString(),oldPassword.text.toString())
            finish()
        }
    }
    fun goBack(view: View) {
        finish()
    }
}
fun change(newPassword:String,password: String)
{
    val request = UserRequest()
    request.password = password
    request.newPassword = newPassword

    val retro = RetrofitClient().getInstance().create(UserService::class.java)
    retro.changepwd(request).enqueue(object : Callback<UserResponse> {
        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
            val user = response.body()
            Log.e("firstname", newPassword.toString())
            if (response.isSuccessful){
                Log.e("firstname", user!!.userr?.firstname!!)
                Log.e("firstname", newPassword)
                Toast.makeText(MyApplication.getInstance(), "Success!!", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(MyApplication.getInstance(), "password is wrong!!", Toast.LENGTH_SHORT).show();
            }
        }
        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            t.message?.let { Log.e("Error", it) }
        }
    })
}