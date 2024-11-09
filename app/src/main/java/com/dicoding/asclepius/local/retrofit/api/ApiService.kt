package com.dicoding.asclepius.local.retrofit.api

import com.dicoding.asclepius.local.retrofit.response.HealthResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines")
    fun getTopHeadlines(
        @Query("q") query: String,
        @Query("category") category: String,
        @Query("language") language: String,
        @Query("apiKey") apiKey: String
    ) : Call<HealthResponse>
}