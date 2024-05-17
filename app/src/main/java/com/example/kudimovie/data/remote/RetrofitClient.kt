package com.example.kudimovie.data.remote

import com.example.kudimovie.services.MovieApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val apiKey = "xxx"
val tmdbBaseUrl = "https://api.themoviedb.org/3/"

val retrofit = Retrofit.Builder()
    .baseUrl(tmdbBaseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val movieApiService = retrofit.create(MovieApiService::class.java)