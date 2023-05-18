package com.clothy.clothyandroid.services

import com.example.clothy.Model.UserResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MatchResponse {
@SerializedName("match")
@Expose
var match: Match? =null

class Match {
    @SerializedName("_id")
    @Expose
    var Id: String? = null
    @SerializedName("IdSession")
    @Expose
    var IdSession: String? = null

    @SerializedName("IdReciver")
    @Expose
    var IdReciver: String? = null

    @SerializedName("IdOutfit")
    @Expose
    var IdOutfit: Array<String>? =null

    @SerializedName("IdOutfitR")
    @Expose
    var IdOutfitR: Array<String>? =null

    @SerializedName("Etat")
    @Expose
    var Etat: Boolean? = null

    @SerializedName("totrade")
    @Expose
    var totrade: String? = null

    @SerializedName("totradeR")
    @Expose
    var totradeR: String? = null

    @SerializedName("trader")
    @Expose
    var trader: String? = null
    @SerializedName("userr")
    @Expose
    var userr: UserResponse.User? =null
}
}