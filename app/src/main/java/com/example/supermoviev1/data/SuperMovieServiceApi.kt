package com.example.supermoviev1.data

import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Path



interface SuperMovieServiceApi {

    @GET("search/{name}")
    suspend fun searchByName(@Path("name") query:String) : retrofit2.Response<SuperMovieResponse>

    @GET("{id}")
    suspend fun findById(@Path("id") identifier: String): Response<Supermovie>

