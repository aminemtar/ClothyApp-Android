package com.example.clothy.ui

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.clothy.clothyandroid.entities.TinderCard
import com.clothy.clothyandroid.services.OutfitResponse
import com.clothy.clothyandroid.services.OutfitService
import com.example.clothy.Model.RetrofitClient
import com.example.clothy.R
import com.example.clothy.Utils
import com.mindorks.placeholderview.SwipeDecor
import com.mindorks.placeholderview.SwipePlaceHolderView
import com.mindorks.placeholderview.SwipeViewBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [SwipeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SwipeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var rootLayout: View? = null
    private lateinit var mSwipeView: SwipePlaceHolderView
    private var mContext: Context? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_swipe_view, container, false)
        return rootLayout
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSwipeView = view.findViewById(R.id.swipeView)

        mContext = activity
        val bottomMargin: Int = Utils.dpToPx(100)
        val windowSize: Point = Utils.getDisplaySize(requireActivity().windowManager)
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
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id","")
        val retro = RetrofitClient().getInstance().create(OutfitService::class.java)
        retro.getoutfitbytype("shoes").enqueue(object : Callback<List<OutfitResponse.Outfit>> {
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
                            mSwipeView.addView<Any>(TinderCard(requireContext(), userr , mSwipeView))
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