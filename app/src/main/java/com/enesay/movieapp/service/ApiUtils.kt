package com.enesay.movieapp.service

import com.enesay.movieapp.utils.Constants.BASE_URL

class ApiUtils {
    companion object{
        fun getKisilerServis() : MovieService {
            return RetrofitClient.getClient(BASE_URL).create(MovieService::class.java)
        }
    }
}