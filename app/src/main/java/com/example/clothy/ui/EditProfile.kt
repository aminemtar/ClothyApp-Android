package com.example.clothy.ui

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.clothy.Model.RetrofitClient
import com.example.clothy.Model.UserRequest
import com.example.clothy.Model.UserResponse
import com.example.clothy.R
import com.example.clothy.Service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EditProfile : AppCompatActivity() {
    lateinit var birthdate: EditText
    lateinit var firstname: EditText
    lateinit var lastname: EditText
    lateinit var pseudo: EditText
    lateinit var phone : EditText
    lateinit var Done : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        firstname =findViewById(R.id.first)
        lastname = findViewById(R.id.last)
        pseudo = findViewById(R.id.pseudo)
        phone = findViewById(R.id.phone)
        val tele = phone.text.toString()
        birthdate = findViewById(R.id.birthdate)
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

            // Set maximum date limit
            datePickerDialog.datePicker.maxDate = c.timeInMillis

            datePickerDialog.show()
        }
        Done= findViewById(R.id.btnDone)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val F = sharedPreferences?.getString("firstname", "")
        val P = sharedPreferences?.getString("pseudo", "")
        val L = sharedPreferences?.getString("lastname", "")
        val PH = sharedPreferences?.getString("phone", "")
        val bd=sharedPreferences?.getString("birthdate", "")
        firstname.setText(F.toString())
        pseudo.setText(P.toString())
        lastname.setText(L.toString())
        phone.setText(PH.toString())
        val date = bd.toString().split("T")[0]
        birthdate.setText(date)

        Done.setOnClickListener {
            update(firstname.text.toString(),lastname.text.toString(),pseudo.text.toString(),phone.text.toString().toInt(),birthdate.text.toString())
            finish()
        }
    }
    fun update(firstname:String,lastname:String,pseudo:String,phone:Int,birthdate:String)
    {
        val request = UserRequest()


        request.phone = phone
        request.pseudo= pseudo
        request.firstname= firstname
        request.lastname = lastname
        request.birthdate= birthdate

        val retro = RetrofitClient().getInstance().create(UserService::class.java)
        retro.update(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {

                val user = response.body()
                if (response.isSuccessful) {
                    Log.e("firstname", user!!.userr?.firstname!!)
                    Log.e("email", user!!.userr?.email!!)
                    Log.e("lastname", user!!.userr?.lastname!!)
                    Log.e("phone", user!!.userr?.phone!!.toString())
                    Log.e("gender", user!!.userr?.gender!!)
                    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    // Save the user's email address in shared preferences
                    editor.putString("email", user!!.userr?.email!!)
                    editor.putString("firstname", user!!.userr?.firstname!!)
                    editor.putString("lastname", user!!.userr?.lastname!!)
                    editor.putString("phone", user!!.userr?.phone!!.toString())
                    editor.putString("pseudo", user!!.userr?.pseudo!!)
                    // Commit the changes to the SharedPreferences object
                    editor.apply()
                }


            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t.message?.let { Log.e("Error", it) }
            }
        })
    }
    fun goBack(view: View) {
        finish()
    }
}