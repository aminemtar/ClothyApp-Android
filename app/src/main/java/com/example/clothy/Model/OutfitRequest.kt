package com.clothy.clothyandroid.services

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OutfitRequest {
    @SerializedName("typee")
    @Expose
    var type:String? =null
    @SerializedName("couleur")
    @Expose
    var couleur:String? =null
    @SerializedName("category")
    @Expose
    var category:String? =null
    @SerializedName("photo")
    @Expose
    var photo:String? =null
    @SerializedName("taille")
    @Expose
    var taille:String? =null
    @SerializedName("userID")
    @Expose
    var userID:String? =null
    @SerializedName("_id")
    @Expose
    var id:String? =null

}