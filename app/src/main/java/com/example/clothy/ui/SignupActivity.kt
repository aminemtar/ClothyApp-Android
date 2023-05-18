package com.example.clothy.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
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
import retrofit2.Call
import retrofit2.Response
import java.util.*
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    private lateinit var email: TextInputEditText
    private lateinit var firstname: TextInputEditText
    private lateinit var lastname: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var newpassword: TextInputEditText
    private lateinit var phone: TextInputEditText
    private lateinit var pseudo: TextInputEditText
    private lateinit var birthdate: TextInputEditText
    private lateinit var login: TextView
    private lateinit var gender: RadioGroup
    private lateinit var register: Button
    private lateinit var gen:String

    private lateinit var player: ExoPlayer
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        email =findViewById(R.id.RegisterEmail)
        firstname=findViewById(R.id.firstname)
        lastname=findViewById(R.id.lastname)
        password = findViewById(R.id.RegisterPassword)
        pseudo=findViewById(R.id.pseudo)
        birthdate= findViewById(R.id.birthdate)
        newpassword=findViewById(R.id.confirmRegisterPassword)
        gender=findViewById(R.id.radio_group_gender)
        login=findViewById(R.id.txtLogin)
        register = findViewById(R.id.btnSubmit)

        gender.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_button_male -> {
                    gen ="Male"
                }
                R.id.radio_button_female -> {
                    gen="Female"
                }
                R.id.radio_button_other -> {
                    gen="Other"
                }
            }
        }
login.setOnClickListener {
    startActivity(Intent(this,LoginActivity::class.java))
}
        birthdate.isFocusable = false  // Disable focus
        birthdate.isClickable = true   // Allow clicks

        birthdate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, yearSelected, monthOfYear, dayOfMonth ->
                val selectedDate = "$dayOfMonth-${monthOfYear + 1}-$yearSelected"
                birthdate.setText(selectedDate)
            }, year, month, day)
            datePickerDialog.datePicker.maxDate = c.timeInMillis
            datePickerDialog.show()
        }
        register.setOnClickListener {
            val emailInput = email.text.toString().trim()
            val (isEmailValid, emailError) = validateEmail(emailInput)
            if (!isEmailValid) {
                email.error = emailError
            }else if (firstname.text.toString().isEmpty()){
                firstname.error="Firstname should not be empty"
            }else if (lastname.text.toString().isEmpty()){
                lastname.error="Lastname should not be empty"
            }
            else if (pseudo.text.toString().isEmpty()){
                pseudo.error="Pseudo should not be empty"
            }else if (email.text.toString().isEmpty()){
                email.error="Email should not be empty"
            }else if(birthdate.text.toString().isEmpty()){
                birthdate.error = "Select a birthdate"
            }else if (password.text.toString().isEmpty()){
                password.error="Password should not be empty"
            }else if(newpassword.text.toString().isEmpty()){
                newpassword.error="New Password should not be empty"
            }else{
                register(email.text.toString(),firstname.text.toString(),lastname.text.toString(),pseudo.text.toString(),password.text.toString(),gen,birthdate.text.toString())
                startActivity(Intent(this,LoginActivity::class.java))
            }
        }
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (password.text.toString() == newpassword.text.toString() && newpassword.text.toString().isNotEmpty()&& password.text.toString().isNotEmpty()) {
                    register.isEnabled = true
                    register.setBackgroundColor(Color.BLACK)
                    register.setTextColor(Color.WHITE)
                } else {
                    register.isEnabled = false
                    register.setBackgroundColor(Color.GRAY)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
        password.addTextChangedListener(textWatcher)
        newpassword.addTextChangedListener(textWatcher)
        //background vid
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
private fun getVideoUri(): Uri {
    val rawId = R.raw.clouds
    val videoUri = "android.resource://com.example.clothy.ui/$rawId"
    return Uri.parse(videoUri)
}
fun register(
    email: String,
    firstname: String,
    lastname: String,
    pseudo:String,
    password: String,
    gender: String,
    birthdate: String,
)
{
    val request = UserRequest()
    request.email = email
    request.pseudo
    request.password =password
    request.pseudo=pseudo
    request.gender = gender
    request.firstname= firstname
    request.lastname = lastname
    request.birthdate= birthdate

    val retro = RetrofitClient().getInstance().create(UserService::class.java)
    retro.register(request).enqueue(object : retrofit2.Callback<UserResponse> {
        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
            if(response.isSuccessful){
                val user = response.body()
                Log.e("firstname", user!!.userr?.firstname!!)
                Log.e("email", user.userr?.email!!)
                Log.e("lastname", user.userr?.lastname!!)
                Log.e("gender", user.userr?.gender!!)
            } else {
                Log.e("mataaditch","please check email")
            }
        }

        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            t.message?.let { Log.e("Error", it) }
        }
    })
}

fun isValidEmail(emailStr: String?) =
    Pattern
        .compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
        ).matcher(emailStr).find()

private fun validateEmail(email: String): Pair<Boolean, String> {
    return if (email.isEmpty()) {
        Pair(false, "Email is required.")
    } else if (!isValidEmail(email)) {
        Pair(false, "Invalid email address.")
    } else {
        Pair(true, "")
    }
}