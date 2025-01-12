package com.enesay.movieapp.data.model

data class Order(
    var orderId : String = "",
    val orderDate: String = "",
    val orderContents: List<Movie> = listOf()
)