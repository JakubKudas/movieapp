package com.example.kudimovie.services

import com.example.kudimovie.data.model.Movie
import com.example.kudimovie.data.remote.MovieListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("movie/popular?language=pl")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String): Response<MovieListResponse>
    @GET("movie/now_playing?language=pl")
    suspend fun getPlayingMovies(@Query("api_key") apiKey: String): Response<MovieListResponse>
    @GET("search/movie?language=pl")
    suspend fun getSearchMovies(@Query("api_key") apiKey: String, @Query("query") query: String): Response<MovieListResponse>
    @GET("movie/{movie_id}?language=pl")
    suspend fun getMovie(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String) :Response<Movie>
}