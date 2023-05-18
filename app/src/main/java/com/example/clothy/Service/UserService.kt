package com.example.clothy.Service

import com.example.clothy.Model.UserRequest
import com.example.clothy.Model.UserResponse
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
data class UploadResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String
)
interface UserService {

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("api/login")
    fun login(
        @Body userRequest: UserRequest
    ): Call<UserResponse>
    @POST("api/register")
    fun register(
        @Body userRequest: UserRequest
    ): Call<UserResponse>
    @GET("api/allUser")
    fun getUsers(): Call<List<UserResponse.User>>
    @PUT("api/updateU")
    fun update( @Body userRequest: UserRequest): Call<UserResponse>
    @POST("api/forgetpwd")
    fun forgetpass(
        @Body userRequest: UserRequest
    ): Call<UserResponse>
    @POST("api/changepwcode")
    fun confirmcode(
        @Body userRequest: UserRequest
    ): Call<UserResponse>
    @PUT("api/changepass")
    fun Resetpwd(
        @Body userRequest: UserRequest
    ): Call<UserResponse>
    @PUT("api/updatepass")
    fun changepwd(
        @Body userRequest: UserRequest
    ): Call<UserResponse>
    @Multipart
    @POST("api/updateImage")
    fun uploadImage(
        @Part imageF: MultipartBody.Part,
    ): Call<ResponseBody>
    @GET("api/once")
    fun user(): Call<UserResponse.User>
}