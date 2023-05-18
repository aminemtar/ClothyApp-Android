package com.clothy.clothyandroid.services

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface OutfitService {
    @DELETE("outfit/delete/{id}")
    fun Delete(@Path("id") id: String): Call<OutfitResponse.Outfit>
    @PUT("outfit/lock/{id}")
    fun lock(@Path("id") id: String): Call<OutfitResponse.Outfit>
    @PUT("outfit/unlock/{id}")
    fun unlock(@Path("id") id: String): Call<OutfitResponse.Outfit>
    @Headers("Content-Type: application/json; charset=utf-8")

    @GET("outfit/allOFT")
    fun getoutfit(): Call<List<OutfitResponse.Outfit>>
    @Headers("Content-Type: application/json; charset=utf-8")

    @GET("outfit/OFT/{typee}")
    fun getoutfitbytype(@Path("typee") type :String): Call<List<OutfitResponse.Outfit>>
    @GET("outfit/getmatcherswiped/{id}")
    fun getuserswiped(@Path("id") id :String): Call<List<OutfitResponse.Outfit>>
    @GET("outfit/getlocked")
    fun getAlluserswiped(): Call<List<OutfitResponse.Outfit>>
    @GET("outfit/once/{id}")
    fun getSelectedOutfit(@Path("id") type :String): Call<OutfitResponse.Outfit>
    @GET("outfit/getall")
    fun getUserOutfit(): Call<List<OutfitResponse.Outfit>>
    @GET("outfit/getswipedd")
    fun getSwiped(): Call<List<OutfitResponse.Outfit>>

        @Multipart
        @POST("outfit/addOutfit")
        fun uploadImage(
            @Part photo: MultipartBody.Part,
            @Part("typee") typee: RequestBody,
            @Part("taille") taille: RequestBody,
            @Part("couleur") couleur: RequestBody,
            @Part("category") category: RequestBody
        ): Call<ResponseBody>


}