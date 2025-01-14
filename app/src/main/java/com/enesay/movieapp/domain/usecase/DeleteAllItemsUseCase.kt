package com.enesay.movieapp.domain.usecase

import com.enesay.movieapp.data.model.MovieCart
import com.enesay.movieapp.data.repository.MovieRepository
import com.enesay.movieapp.domain.repository.CartRepository
import javax.inject.Inject

class DeleteAllItemsUseCase @Inject constructor(private val cartRepository: CartRepository){
    suspend fun deleteAllItemsCart(movieList: List<MovieCart>, username: String): Result<Unit> {
        return try {
            cartRepository.deleteAllCartItems(movieList, username)
            Result.success(Unit) // İşlem başarılıysa
        } catch (e: Exception) {
            Result.failure(e) // Hata durumunda
        }
    }
}