package com.enesay.movieapp.data.model

data class Movie(
    val id: Int = 0,
    val name: String = "",
    val category: String = "",
    val description: String = "",
    val director: String = "",
    val image: String = "",
    val price: Int = 0,
    val rating: Double = 0.0,
    val year: Int = 0
)