package com.example.clothy.Model

import com.clothy.clothyandroid.services.MatchResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class MatchResponsee(
    @SerializedName("doc") val doc: List<MatchResponse.Match>,
    @SerializedName("users") val users: List<UserResponse.User>
)
class UserResponse {
    @SerializedName("userr")
    @Expose
    var userr: User? =null
    class User{
        @SerializedName("email")
        @Expose
        var email:String? =null
        @SerializedName("firstname")
        @Expose
        var firstname:String? =null
        @SerializedName("lastname")
        @Expose
        var lastname:String? =null
        @SerializedName("phone")
        @Expose
        var phone:Int? =null
        @SerializedName("gender")
        @Expose
        var gender:String? =null
        @SerializedName("birthdate")
        @Expose
        var birthdate:String? =null
        @SerializedName("imageF")
        @Expose
        var image:String? =null
        @SerializedName("_id")
        @Expose
        var id:String? =null
        @SerializedName("pseudo")
        @Expose
        var pseudo:String? =null
    }
}