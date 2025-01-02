package com.enesay.movieapp.data.model

data class Movie(
    val category: String,
    val description: String,
    val director: String,
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val rating: Double,
    val year: Int
)