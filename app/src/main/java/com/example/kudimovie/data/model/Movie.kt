package com.example.kudimovie.data.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("vote_average") val voteAverage: Float,
    @SerializedName("runtime") val runtime: Int,
    @SerializedName("overview") val overview: String,
    @SerializedName("genres") val genres: List<Genre>
)


