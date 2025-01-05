package com.enesay.movieapp.data.model

data class Movie(
    val id: Int,
    val name: String,
    val category: String,
    val description: String,
    val director: String,
    val image: String,
    val price: Int,
    val rating: Double,
    val year: Int
)