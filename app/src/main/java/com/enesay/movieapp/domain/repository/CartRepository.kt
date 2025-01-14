package com.enesay.movieapp.domain.repository

import com.enesay.movieapp.data.model.MovieActionResponse
import com.enesay.movieapp.data.model.MovieCart
import retrofit2.Response

interface CartRepository {
    suspend fun deleteAllCartItems(movieIdList: List<MovieCart>, userName: String) : Response<MovieActionResponse>
}