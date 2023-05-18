package com.example.clothy.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.manager.Lifecycle
import com.clothy.clothyandroid.services.OutfitRequest
import com.clothy.clothyandroid.services.OutfitResponse
import com.clothy.clothyandroid.services.OutfitService
import com.example.clothy.MainActivity
import com.example.clothy.Model.MyApplication
import com.example.clothy.Model.RetrofitClient
import com.example.clothy.Model.UserRequest
import com.example.clothy.Model.UserResponse
import com.example.clothy.R
import com.example.clothy.Service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OutfitDetailActivity : AppCompatActivity() {
    private lateinit var type : TextView
    private lateinit var XS : Button
    private lateinit var S : Button
    private lateinit var M : Button
    private lateinit var L : Button
    private lateinit var XL : Button
    private lateinit var image : ImageView
    private lateinit var catg: TextView
    private lateinit var  color : ImageView
    private lateinit var delete:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outfit_detail)
        type= findViewById(R.id.txtProductDetailName)
        image =findViewById(R.id.imgProductDetail)
        catg =findViewById(R.id.txtProductDetailDescription)
        color =findViewById(R.id.product_color)
        XS = findViewById(R.id.btnSizeXS)
        S = findViewById(R.id.btnSizeS)
        M =findViewById(R.id.btnSizeM)
        L= findViewById(R.id.btnSizeL)
        XL = findViewById(R.id.btnSizeXL)
        delete=findViewById(R.id.btnAddToCart)
        val id = intent.getStringExtra("ITEM_ID")
        val retro = RetrofitClient().getInstance().create(OutfitService::class.java)
        Log.d("ItemId", id.toString())
        retro.getSelectedOutfit(id.toString()).enqueue(object : Callback<OutfitResponse.Outfit> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<OutfitResponse.Outfit>,
                response: Response<OutfitResponse.Outfit>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()
                    // user?.get(0)?.Etat?.let { Log.e("type", it.toString()) }
                    if (user != null) {
                        Log.e("type",user.type.toString())
                        if(user.type.toString()=="shoes")
                        {
                            Log.e("taille",user.taille.toString())

                            M.setBackgroundColor(Color.rgb(225, 155, 155))
                            M.text = user.taille.toString()
                            XS.text = (user.taille!!.toInt() - 2).toString()
                            S.text = (user.taille!!.toInt() - 1).toString()
                            L.text = (user.taille!!.toInt() + 1).toString()
                            XL.text = (user.taille!!.toInt() + 2).toString()
                        }

                        if(user.taille.toString()=="XS")
                        {
                            XS.setBackgroundColor(Color.rgb(225, 155, 155))
                        }else if (user.taille.toString()=="S"){
                            S.setBackgroundColor(Color.rgb(225, 155, 155))
                        }else if (user.taille.toString()=="M"){
                            M.setBackgroundColor(Color.rgb(225, 155, 155))
                        }else if (user.taille.toString()=="L"){
                            L.setBackgroundColor(Color.rgb(225, 155, 155))
                        }else if (user.taille.toString()=="XL"){
                            XL.setBackgroundColor(Color.rgb(225, 155, 155))
                        }
                        val Clr = user.couleur.toString()
                        color.setBackgroundColor(Color.parseColor(Clr))
                        type.text = user.type.toString()
                        catg.text =user.category.toString()
                        Glide.with(MyApplication.getInstance())
                            .load(RetrofitClient().BASE_URLL+user.photo)
                            .into(image)

                    }
                }
            }

            override fun onFailure(call: Call<OutfitResponse.Outfit>, t: Throwable) {
                // Handle failure
                Log.e("Error", "error")
            }
        })
        delete.setOnClickListener {
            lifecycleScope.launch {
                delete(id.toString())
                startActivity(Intent(MyApplication.getInstance(),MainActivity::class.java))
            }
        }
    }
    fun goBack(view: View) {
        finish()
    }
     suspend fun delete(id:String): OutfitResponse.Outfit? {
        return withContext(Dispatchers.IO) {
            val request = OutfitRequest()
            request.id = id
            val retro = RetrofitClient().getInstance().create(OutfitService::class.java)
            val response = retro.Delete(id).execute()
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {

                    return@withContext user
                }
            }

            return@withContext null
        }
    }
}