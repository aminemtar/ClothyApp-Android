package com.example.clothy.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.clothy.clothyandroid.services.OutfitResponse
import com.example.clothy.Model.MyApplication
import com.example.clothy.Model.RetrofitClient
import com.example.clothy.R
import com.example.clothy.ui.OutfitDetailActivity
import java.net.HttpURLConnection
import java.net.URL

class OutfitAdapter (private val outfitlist:List<OutfitResponse.Outfit>):  ListAdapter<OutfitResponse.Outfit, OutfitAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<OutfitResponse.Outfit>() {
        override fun areItemsTheSame(a: OutfitResponse.Outfit, b: OutfitResponse.Outfit)    = a.idd == b.idd
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(a: OutfitResponse.Outfit, b: OutfitResponse.Outfit) = a == b

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto     : ImageView = view.findViewById(R.id.outfitImage)
        val txtName      : TextView = view.findViewById(R.id.outfitName)
       val padlockIcon : ImageView= view.findViewById(R.id.padlockIcon);

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.outfit_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = outfitlist[position]

//        holder.txtId.text   = friend.id
        holder.txtName.text = product.type
        holder.txtName.setTypeface(holder.txtName.typeface, Typeface.BOLD_ITALIC)
        holder.txtName.setTextColor(Color.parseColor("#333333"));

        // holder.outfitcolor.setBackgroundColor(Color.parseColor(product.couleur))

//        holder.txtQuantity.text = product.productQuan.toString()

        // TODO: Photo (blob to bitmap)a
        if (product.locked!!) {
            holder.padlockIcon.setVisibility(View.VISIBLE);
        } else {
            holder.padlockIcon.setVisibility(View.GONE);
        }
        Glide.with(MyApplication.getInstance())
            .load(RetrofitClient().BASE_URLL+product.photo)
            .transform(CenterCrop(), RoundedCorners(50))
            .into(holder.imgPhoto)
    }
}