package com.enesay.movieapp.data.model

data class Order(
    val orderId: String = "",
    val orderDate: String = "",
    val orderContents: List<MovieCart> = listOf(),
    val totalPrice: Int = 0
)