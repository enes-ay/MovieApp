package com.enesay.movieapp.data.repository

import com.enesay.movieapp.data.model.Card
import com.enesay.movieapp.data.model.Order

interface PaymentRepository {
    suspend fun addCard(userId: String, card: Card)
    suspend fun deleteCard(userId: String, cardId: String)
    suspend fun updateCard(userId: String, card: Card)
    suspend fun getCards(userId: String): List<Card>

    suspend fun addOrder(userId: String, order: Order)
    suspend fun deleteOrder(userId: String, orderId: String)
    suspend fun updateOrder(userId: String, order: Order)
    suspend fun getOrders(userId: String): List<Order>
}