package com.example.supermoviev1.data

import com.example.supermoviev1.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SuperMovieServiceApi {

    @GET("/?apikey=${Constants.API_ACCESS_TOKEN}")
    suspend fun searchByName(@Query("s") query: String): Response<SuperMovieResponse>

    @GET("/?apikey=${Constants.API_ACCESS_TOKEN}")
    suspend fun findById(@Query("i") identifier: String): Response<Supermovie>

    @GET("movies") // Endpoint para obtener todas las películas
    suspend fun getAllMovies(): Response<List<Supermovie>>
}
