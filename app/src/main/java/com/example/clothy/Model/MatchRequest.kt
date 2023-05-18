package com.example.clothy.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MatchRequest {
    @SerializedName("IdSession")
    @Expose
    var IdSession:String? =null
    @SerializedName("IdReciver")
    @Expose
    var IdReciver:String? =null
    @SerializedName("IdOutfit")
    @Expose
    var IdOutfit:String? =null
    @SerializedName("IdOutfitR")
    @Expose
    var IdOutfitR:String? =null
    @SerializedName("Etat")
    @Expose
    var Etat:Boolean? =null
    @SerializedName("totrade")
    @Expose
    var totrade:Int? =null
    @SerializedName("totradeR")
    @Expose
    var totradeR:String? =null
    @SerializedName("trader")
    @Expose
    var trader:String? =null


}