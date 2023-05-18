package com.example.clothy.Adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.clothy.clothyandroid.*
import com.clothy.clothyandroid.services.*
import com.example.clothy.Model.MyApplication
import com.example.clothy.Model.RetrofitClient
import com.example.clothy.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LockedAdapter (private val outfitlist:List<OutfitResponse.Outfit>):  ListAdapter<OutfitResponse.Outfit, LockedAdapter.ViewHolder>(LockedAdapter)  {

    companion object DiffCallback : DiffUtil.ItemCallback<OutfitResponse.Outfit>() {
        override fun areItemsTheSame(a: OutfitResponse.Outfit, b: OutfitResponse.Outfit)    = a.idd == b.idd
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(a: OutfitResponse.Outfit, b: OutfitResponse.Outfit) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val cv           : CardView = view.findViewById(R.id.cv)
        val unlock    : Button = view.findViewById(R.id.unlock)
        val imgPhoto     : ImageView = view.findViewById(R.id.imgVoucher)
        val type      : TextView = view.findViewById(R.id.type)
        val Etat    : TextView = view.findViewById(R.id.Etat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.activity_item_locked, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val outfit = getItem(position)
        val context = holder.itemView.context
        Glide.with(MyApplication.getInstance())
            .load(RetrofitClient().BASE_URLL+outfit.photo)
            .transform(CenterCrop(), RoundedCorners(50))
            .into(holder.imgPhoto)
        holder.type.text = outfit.type
        if (outfit.locked!!){
            holder.Etat.text = "Status:Locked "
            holder.unlock.setBackgroundResource(R.drawable.ic_padlock)
            //holder.Etat.setTextColor(android.graphics.Color.parseColor("#FF0000"))
        }else{
            holder.Etat.text = "Status:UnLocked "
            //holder.Etat.setTextColor(android.graphics.Color.parseColor("#00A300"))
            holder.unlock.setBackgroundResource(R.drawable.unlock)

        }
        var lock = outfit.locked
        holder.unlock.setOnClickListener {
            if (lock == true){
                lock =false
                //  holder.Etat.setTextColor(android.graphics.Color.parseColor("#00A300"))
                holder.Etat.text = "Status:UnLocked"
                holder.unlock.setBackgroundResource(R.drawable.unlock)
                val retro = RetrofitClient().getInstance().create(OutfitService::class.java)
                retro.unlock(outfit.idd!!).enqueue(object : Callback<OutfitResponse.Outfit> {
                    override fun onResponse(call: Call<OutfitResponse.Outfit>, response: Response<OutfitResponse.Outfit>) {
                        if (response.isSuccessful) {
                            val user = response.body()
                            // user?.get(0)?.Etat?.let { Log.e("type", it.toString()) }
                            if (user != null) {
                                println(user.toString())
                            }
                        }
                    }

                    override fun onFailure(call: Call<OutfitResponse.Outfit>, t: Throwable) {
                        Log.e("Error", "error")
                    }
                })
            }else{
                holder.Etat.text = "Status:Locked"
                lock=true
                holder.unlock.setBackgroundResource(R.drawable.ic_padlock)
                //holder.Etat.setTextColor(android.graphics.Color.parseColor("#FF0000"))
                val retro = RetrofitClient().getInstance().create(OutfitService::class.java)
                retro.lock(outfit.idd!!).enqueue(object : Callback<OutfitResponse.Outfit> {
                    override fun onResponse(call: Call<OutfitResponse.Outfit>, response: Response<OutfitResponse.Outfit>) {
                        if (response.isSuccessful) {
                            val user = response.body()
                            // user?.get(0)?.Etat?.let { Log.e("type", it.toString()) }
                            if (user != null) {
                                println(user.toString())
                            }
                        }
                    }

                    override fun onFailure(call: Call<OutfitResponse.Outfit>, t: Throwable) {
                        Log.e("Error", "error")
                    }
                })
            }

        }
    }
}
