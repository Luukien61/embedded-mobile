package com.example.lab3embedded.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val okHttpClient: OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(15, TimeUnit.SECONDS) // Kết nối tối đa 15 giây
    .readTimeout(30, TimeUnit.SECONDS)    // Chờ phản hồi tối đa 30 giây
    .writeTimeout(60, TimeUnit.SECONDS)   // Gửi dữ liệu tối đa 60 giây
    .build();

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.21.81:8080"

    private var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}