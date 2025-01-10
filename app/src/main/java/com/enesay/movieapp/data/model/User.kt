package com.enesay.movieapp.data.model

    data class User(
        val name: String = "",
        val surname: String = "",
        val email: String = "",
        val favorites: List<Map<String, Any>> = emptyList()
    )