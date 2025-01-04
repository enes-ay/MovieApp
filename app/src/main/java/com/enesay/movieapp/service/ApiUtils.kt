package com.enesay.movieapp.service

import com.enesay.movieapp.utils.Constants.BASE_URL

class ApiUtils {
    companion object{
        fun getMovieService() : MovieService {
            return RetrofitClient.getClient().create(MovieService::class.java)
        }
    }
}