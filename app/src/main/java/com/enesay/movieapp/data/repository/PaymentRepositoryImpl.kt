package com.enesay.movieapp.data.repository

import com.enesay.movieapp.data.model.Card
import com.enesay.movieapp.data.model.Order
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(private val usersCollection: CollectionReference) : PaymentRepository {
    override suspend fun addCard(userId: String, card: Card) {
        usersCollection.document(userId).collection("cards").add(card).await()
    }

    override suspend fun deleteCard(userId: String, cardId: String) {
        usersCollection.document(userId).collection("cards").document(cardId).delete().await()
    }

    override suspend fun updateCard(userId: String, card: Card) {
        usersCollection.document(userId).collection("cards").document(card.cardId).set(card).await()
    }

    override suspend fun getCards(userId: String): List<Card> {
        val snapshot = usersCollection.document(userId).collection("cards").get().await()
        return snapshot.documents.mapNotNull { it.toObject(Card::class.java)?.copy(cardId = it.id) }
    }

    override suspend fun addOrder(userId: String, order: Order) {
        usersCollection.document(userId).collection("orders").add(order).await()
    }

    override suspend fun deleteOrder(userId: String, orderId: String) {
        usersCollection.document(userId).collection("orders").document(orderId).delete().await()
    }

    override suspend fun updateOrder(userId: String, order: Order) {
        usersCollection.document(userId).collection("orders").document(order.orderId).set(order).await()
    }

    override suspend fun getOrders(userId: String): List<Order> {
        val snapshot = usersCollection.document(userId).collection("orders").get().await()
        return snapshot.documents.mapNotNull { it.toObject(Order::class.java)?.copy(orderId = it.id) }
    }
}