package com.example.clothy.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import androidx.lifecycle.lifecycleScope
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
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var email : TextInputEditText
    private lateinit var code : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var newpassword : TextInputEditText
    private lateinit var emailL: TextInputLayout
    private lateinit var codeL : TextInputLayout
    private lateinit var passwordL : TextInputLayout
    private lateinit var newpasswordL : TextInputLayout
    private lateinit var submit : Button
    private lateinit var player: ExoPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        email = findViewById(R.id.old)
        emailL = findViewById(R.id.oldpassword)
        codeL = findViewById(R.id.edtcode)
        passwordL = findViewById(R.id.edtPassword)
        newpasswordL = findViewById(R.id.edtConfirmPassword)
        code = findViewById(R.id.code)
        password = findViewById(R.id.newP)
        newpassword = findViewById(R.id.CnewP)
        submit = findViewById(R.id.btnSubmitt)
        codeL.visibility = View.GONE
        passwordL.visibility = View.GONE
        newpasswordL.visibility = View.GONE
        val playerView = findViewById<PlayerView>(R.id.player_view)
        player = ExoPlayer.Builder(this).build()
        playerView.player = player
        playerView.useController = false
        player.repeatMode = Player.REPEAT_MODE_ALL

        val mediaItem = MediaItem.fromUri(getVideoUri())
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        submit.setOnClickListener {
            lifecycleScope.launch {
                if (email.text.toString().isEmpty()) {
                    email.error = "Please enter your email"

                } else if(email.text.toString().isNotEmpty() && codeL.visibility== View.GONE ) {
                   val response= sendCode(email.text.toString())
                    if (response != null) {
                        codeL.visibility = View.VISIBLE
                        submit.setOnClickListener {
                            lifecycleScope.launch {
                                if (code.text.toString().isNotEmpty()) {
                                    val confirmResponse =
                                        confirmCode(
                                            email.text.toString(),
                                            code.text.toString().toInt()
                                        )
                                    if (confirmResponse != null) {
                                        emailL.visibility = View.GONE
                                        codeL.visibility = View.GONE
                                        passwordL.visibility = View.VISIBLE
                                        newpasswordL.visibility = View.VISIBLE
                                        submit.setBackgroundColor(Color.GRAY)
                                        val textWatcher = object : TextWatcher {
                                            override fun beforeTextChanged(
                                                s: CharSequence,
                                                start: Int,
                                                count: Int,
                                                after: Int
                                            ) {
                                            }

                                            override fun onTextChanged(
                                                s: CharSequence,
                                                start: Int,
                                                before: Int,
                                                count: Int
                                            ) {
                                                if (password.text.toString() == newpassword.text.toString() && newpassword.text.toString()
                                                        .isNotEmpty() && password.text.toString()
                                                        .isNotEmpty()
                                                ) {
                                                    submit.isEnabled = true
                                                    submit.setBackgroundColor(Color.BLACK)
                                                } else {
                                                    submit.isEnabled = false
                                                    submit.setBackgroundColor(Color.GRAY)
                                                }
                                            }

                                            override fun afterTextChanged(s: Editable) {}
                                        }

                                        password.addTextChangedListener(textWatcher)
                                        newpassword.addTextChangedListener(textWatcher)
                                        submit.setOnClickListener {
                                            lifecycleScope.launch {
                                                resetpwd(password.text.toString())
                                                startActivity(Intent(MyApplication.getInstance(),LoginActivity::class.java))
                                            }}
                                    } else {
                                        code.error = "Code is incorrect"
                                    }
                                } else {
                                    code.error = "Please enter your code"
                                }
                            }
                        }

                    } else {
                        email.error = "Email not Found"
                    }

                }
            }

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    override fun onStop() {
        super.onStop()
        player.stop()
    }
    private suspend fun sendCode(email: String): UserResponse? = withContext(Dispatchers.IO) {
        val request = UserRequest()
        request.email = email
        val retro = RetrofitClient().getInstance().create(UserService::class.java)

        try {
            val response = retro.forgetpass(request).execute()
            if (response.isSuccessful) {
                return@withContext response.body()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return@withContext null
    }
    private suspend fun confirmCode(email: String, code: Int): UserResponse? = withContext(Dispatchers.IO) {
        val request = UserRequest()
        request.email = email
        request.code = code
        val retro = RetrofitClient().getInstance().create(UserService::class.java)

        try {
            val response = retro.confirmcode(request).execute()
            if (response.isSuccessful) {
                return@withContext response.body()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return@withContext null
    }

    private suspend fun resetpwd(newPassword: String): UserResponse? = withContext(Dispatchers.IO) {
        val request = UserRequest()
        request.newPassword = newPassword

        val retro = RetrofitClient().getInstance().create(UserService::class.java)

        try {
            val response = retro.Resetpwd(request).execute()
            if (response.isSuccessful) {
                return@withContext response.body()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return@withContext null
    }

    private fun getVideoUri(): Uri {
        val rawId = R.raw.clouds
        val videoUri = "android.resource://com.example.clothy.ui/$rawId"
        return Uri.parse(videoUri)
    }
    fun goBack(view: View) {
        finish()
    }
}


