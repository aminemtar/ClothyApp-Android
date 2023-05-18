package com.example.clothy.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.clothy.MainActivity
import com.example.clothy.Model.MyApplication
import com.example.clothy.Model.RetrofitClient
import com.example.clothy.Model.UserRequest
import com.example.clothy.Model.UserResponse
import com.example.clothy.R
import com.example.clothy.Service.UserService
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.CookieHandler
import java.net.CookieManager
import java.net.HttpCookie

class LoginActivity : AppCompatActivity() {
    private lateinit var signup : TextView
    private lateinit var btnlogin :Button
    private lateinit var email : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var fpw : Button
    private lateinit var player: ExoPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnlogin= findViewById(R.id.btnLogin)
        email = findViewById(R.id.LoginEmail)
        signup=findViewById(R.id.txtRegister)
        password = findViewById(R.id.LoginPassword)
        fpw = findViewById(R.id.btnForgetPassword)
        fpw.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
            finish()
        }
        btnlogin.setOnClickListener {
            lifecycleScope.launch {
                val input = email.text.toString().trim()
                val pwd = email.text.toString().trim()
                if (!validateInput(input).first) {
                    email.error = validateInput(input).second

                } else if (!validateInput(pwd).first) {
                    password.error = validateInput(pwd).second
                } else {
                    val log = login(email.text.toString(), password.text.toString(), MyApplication.getInstance())
                    if(log != null){
                        val intent = Intent(MyApplication.getInstance(), MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@LoginActivity, "Email or password incorrect", Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }
        signup.setOnClickListener {
            val intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }
        val playerView = findViewById<PlayerView>(R.id.player_view)
        player = ExoPlayer.Builder(this).build()
        playerView.player = player
        playerView.useController = false
        player.repeatMode = Player.REPEAT_MODE_ALL

        val mediaItem = MediaItem.fromUri(getVideoUri())
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    override fun onStop() {
        super.onStop()
        player.stop()
    }

}
suspend fun login(email: String, password: String, context: Context): UserResponse? {
    return withContext(Dispatchers.IO) {
        val request = UserRequest()
        request.email = email
        request.password = password
        val retro = RetrofitClient().getInstance().create(UserService::class.java)
        val response = retro.login(request).execute()
        if (response.isSuccessful) {
            val user = response.body()
            if (user != null) {
                val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("id", user.userr?.id!!)
                editor.putString("email", user.userr?.email!!)
                editor.putString("firstname", user.userr?.firstname!!)
                editor.putString("lastname", user.userr?.lastname!!)
                editor.putString("pseudo", user.userr?.pseudo!!)
                editor.putString("phone", user.userr?.phone!!.toString())
                editor.putString("gander", user.userr?.gender!!)
                editor.putString("image", user.userr?.image!!)
                editor.apply()
                return@withContext user
            }
        }

        return@withContext null
    }
}

fun validateInput(input: String): Pair<Boolean, String> {
    return when {
        input.isEmpty() -> {
            // input is empty
            Pair(false, "Email cannot be empty")
        }
        input.length < 6 -> {
            // input is too short
            Pair(false, "Input must be at least 6 characters long")
        }
        input.contains(" ") -> {
            // input contains whitespace
            Pair(false, "Input cannot contain spaces")
        }
        else -> {
            // input is valid
            Pair(true, "")
        }
    }
}
private fun getVideoUri(): Uri {
    val rawId = R.raw.clouds
    val videoUri = "android.resource://com.example.clothy.ui/$rawId"
    return Uri.parse(videoUri)
}
