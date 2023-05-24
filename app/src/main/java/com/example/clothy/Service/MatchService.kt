package com.example.clothy.Service

import com.clothy.clothyandroid.services.MatchResponse
import com.example.clothy.Model.MatchRequest
import com.example.clothy.Model.Message
import com.example.clothy.Model.UserResponse
import retrofit2.Call
import retrofit2.http.*
data class ApiResponse(
    val doc: List<MatchResponse.Match>,
    val users: List<UserResponse.User>
)
interface MatchService {
    @PUT("match/swipe/{IdReciver}/{id}")
    fun match(
        @Path("IdReciver") IdReciver: String,@Body MatchRequest: MatchRequest
    ): Call<MatchResponse>


    @Headers("Content-Type: application/json; charset=utf-8")

    @GET("match/getmatchs")
    fun getmatchs(): Call<List<MatchResponse.Match>>
    @GET("msg/getmsg/{id}")
    fun getmessages(@Path("id") id: String): Call<List<Message>>

    @Headers("Content-Type: application/json; charset=utf-8")

    @GET("match/getmatch")
    fun getmatch(): Call<List<MatchResponse.Match>>
}