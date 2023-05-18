package com.example.clothy.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.clothy.clothyandroid.services.OutfitResponse
import com.clothy.clothyandroid.services.OutfitService
import com.example.clothy.Adapter.LockedAdapter
import com.example.clothy.Model.MyApplication
import com.example.clothy.Model.RetrofitClient
import com.example.clothy.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class AllLockedActivity : AppCompatActivity() {
    var outfitlist = ArrayList<OutfitResponse.Outfit>()
    private lateinit var adapter: LockedAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var back: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_locked)
        recyclerView = findViewById(R.id.rv)
        adapter= LockedAdapter(outfitlist)
        recyclerView.adapter = adapter
        back=findViewById(R.id.imgCartBack)
        val retro = RetrofitClient().getInstance().create(OutfitService::class.java)
        retro.getAlluserswiped().enqueue(object : Callback<List<OutfitResponse.Outfit>> {
            override fun onResponse(
                call: Call<List<OutfitResponse.Outfit>>,
                response: Response<List<OutfitResponse.Outfit>>
            ) {
                if (response.isSuccessful) {
                    val outfit = response.body()
                    if (outfit != null) {
                        outfitlist.clear() // Clear the existing list
                        outfitlist.addAll(outfit)
                        println(outfit)
                        setupRecyclerViewAdapter()
                        adapter.notifyDataSetChanged()
                    }
                }
                else{
                    Log.e("Error", "error")
                }
            }

            override fun onFailure(call: Call<List<OutfitResponse.Outfit>>, t: Throwable) {
                // Handle failure
            }
        })
        recyclerView = findViewById(R.id.rv)
        outfitlist = ArrayList()
        val layoutParams = recyclerView.layoutParams
        recyclerView.layoutParams = layoutParams
        adapter = LockedAdapter(outfitlist)
        recyclerView.adapter = adapter
        adapter.submitList(outfitlist)
    }
    private fun setupRecyclerViewAdapter() {
        recyclerView = findViewById(R.id.rv)
        adapter = LockedAdapter(outfitlist)
        recyclerView.adapter = adapter
        adapter.submitList(outfitlist)

    }
    fun goBack(view: View) {
        finish()
    }
}