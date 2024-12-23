package com.example.lab3embedded.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class DataResponse(
    val temperature: Float,
    val humidity: Float,
    val button1: Boolean,
    val button2: Boolean
)
data class UserEntity(
    val id: Long?,
    val username: String,
    val password: String,
    val message: String
)

interface ApiService {
    @GET("/app/data")
    suspend fun getData(): DataResponse

    @GET("/app/toggle/{ledId}")
    suspend fun toggleLed(@Path("ledId") ledId: Int)

    @POST("/api/user/login")
    fun login(@Body userEntity: UserEntity): Call<UserEntity>

    @GET("/api/user/{userId}")
    suspend fun getUser(@Path("userId") userId: Long): UserEntity

    @PUT("/api/user/{userId}")
    suspend fun updateUser(@Path("userId") userId: Long, @Body userEntity: UserEntity): UserEntity

}