package com.enesay.movieapp.data.repository_refactor

import com.enesay.movieapp.data.model.MovieActionResponse
import com.enesay.movieapp.data.model.MovieCart
import com.enesay.movieapp.domain.repository.CartRepository
import com.enesay.movieapp.service.MovieService
import retrofit2.Response
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val movieService: MovieService) :
    CartRepository {
    override suspend fun deleteAllCartItems(
        movieIdList: List<MovieCart>,
        movieName: String
    ): Response<MovieActionResponse> {
        try {
            for (movie in movieIdList) {
                val response = movieService.deleteMovie(movie.cartId, movieName)
                if (!response.isSuccessful) {
                    throw Exception(
                        "Failed to delete movie: $movie. Error: ${
                            response.errorBody()?.string()
                        }"
                    )
                }
            }
            return Response.success(MovieActionResponse(1, "All items deleted successfully"))
        } catch (e: Exception) {
            return Response.error(
                500,
                okhttp3.ResponseBody.create(null, e.message ?: "Unknown error occurred")
            )
        }
    }

}