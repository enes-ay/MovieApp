package com.enesay.movieapp.service

import com.enesay.movieapp.data.model.MovieActionResponse
import com.enesay.movieapp.data.model.MovieCartResponse
import com.enesay.movieapp.data.model.MovieResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface MovieService {

    @GET("movies/getAllMovies.php")
    fun getMovies(): Response<MovieResponse>

    @POST("movies/getAllMovieCart.php")
    @FormUrlEncoded

    fun getMovieCart(@Field("username") username: String = "enesay"): Response<MovieCartResponse>

    @POST("movies/insertMovie.php")
    @FormUrlEncoded
    fun insertMovie(
        @Field("name") movie_name: String,
        @Field("image") movie_image: String,
        @Field("price") movie_price: Int,
        @Field("category") movie_category: String,
        @Field("rating") movie_rating: Double,
        @Field("year") movie_year: Int,
        @Field("director") movie_director: String,
        @Field("description") movie_description: String,
        @Field("orderAmount") order_amount: Int,
        @Field("userName") user_name: String
    ): Response<MovieActionResponse>

    @POST("movies/deleteMovie.php")
    @FormUrlEncoded
    fun deleteMovie(
        @Field("cartId") cart_id: String, @Field("userName") user_name: String
    ): Response<MovieActionResponse>

}