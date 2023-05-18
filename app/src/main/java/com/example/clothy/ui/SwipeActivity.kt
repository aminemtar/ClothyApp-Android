package com.example.clothy.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.clothy.clothyandroid.entities.TinderCard
import com.clothy.clothyandroid.services.OutfitResponse
import com.clothy.clothyandroid.services.OutfitService
import com.example.clothy.Model.MyApplication
import com.example.clothy.Model.RetrofitClient
import com.example.clothy.R
import com.example.clothy.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mindorks.placeholderview.SwipeDecor
import com.mindorks.placeholderview.SwipePlaceHolderView
import com.mindorks.placeholderview.SwipeViewBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SwipeActivity: AppCompatActivity() {
    private var rootLayout: View? = null
    private lateinit var mSwipeView: SwipePlaceHolderView
    private var mContext: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_swipe_view)
      val  mSwipeView = findViewById<SwipePlaceHolderView>(R.id.swipeView)

        mContext = this
        val bottomMargin: Int = Utils.dpToPx(100)
        val windowSize: Point = Utils.getDisplaySize(this.windowManager)
        mSwipeView.getBuilder<SwipePlaceHolderView, SwipeViewBuilder<SwipePlaceHolderView>>()
            .setDisplayViewCount(3)
            .setSwipeDecor(
                SwipeDecor()
                    .setViewWidth(windowSize.x)
                    .setViewHeight(windowSize.y - bottomMargin)
                    .setViewGravity(Gravity.TOP)
                    .setPaddingTop(20)
                    .setRelativeScale(0.01f)
                    .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                    .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view)
            )
        val myValue = intent.getStringExtra("type")

        val sharedPreferences = mContext?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("id","")
        val retro = RetrofitClient().getInstance().create(OutfitService::class.java)
        retro.getoutfitbytype(myValue.toString()).enqueue(object : Callback<List<OutfitResponse.Outfit>> {
            override fun onResponse(
                call: Call<List<OutfitResponse.Outfit>>,
                response: Response<List<OutfitResponse.Outfit>>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()

                    //  user?.get(0)?.type?.let { Log.e("type", it) }
                    if (user != null) {
                        for (userr in user) {
                            userr.category?.let { Log.e("category", it) }
                            mSwipeView.addView<Any>(TinderCard(this@SwipeActivity, userr , mSwipeView))
                            println(userr)
                        }
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
    }
}
