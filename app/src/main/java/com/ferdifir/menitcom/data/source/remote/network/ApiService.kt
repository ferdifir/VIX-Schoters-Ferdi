package com.ferdifir.menitcom.data.source.remote.network

import com.ferdifir.menitcom.data.source.remote.response.NewsResponse
import retrofit2.http.*

interface ApiService {

    @GET("top-headlines")
    suspend fun getTopHeadlineNews(
        @Header("Authorization") token: String,
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("pageSize") pageSize: Int = 10
    ): NewsResponse

    @GET("everything")
    suspend fun getAllNews(
        @Header("Authorization") token: String,
        @Query("q") query: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("sortBy") sortBy: String,
        @Query("language") language: String,
        @Query("pageSize") pageSize: Int = 100
    ): NewsResponse
}