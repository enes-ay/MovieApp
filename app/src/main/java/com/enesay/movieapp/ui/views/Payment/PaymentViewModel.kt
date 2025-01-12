package com.enesay.movieapp.ui.views.Payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enesay.movieapp.data.model.Card
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.data.model.Order
import com.enesay.movieapp.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(private val repository: PaymentRepository) :
    ViewModel() {
    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> get() = _cards

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> get() = _orders

    fun fetchCards(userId: String) {
        viewModelScope.launch {
            _cards.value = repository.getCards(userId)
        }
    }

    fun fetchOrders(userId: String) {
        viewModelScope.launch {
            _orders.value = repository.getOrders(userId)
        }
    }

    fun addCard(userId: String, card: Card) {
        viewModelScope.launch {
            repository.addCard(userId, card)
            fetchCards(userId)
        }
    }

    fun deleteCard(userId: String, cardId: String) {
        viewModelScope.launch {
            repository.deleteCard(userId, cardId)
            fetchCards(userId)
        }
    }

    fun updateCard(userId: String, card: Card) {
        viewModelScope.launch {
            repository.updateCard(userId, card)
            fetchCards(userId)
        }
    }

    fun addOrder(userId: String, order: Order) {
        viewModelScope.launch {
            repository.addOrder(userId, order)
            fetchOrders(userId)
        }
    }

    fun deleteOrder(userId: String, orderId: String) {
        viewModelScope.launch {
            repository.deleteOrder(userId, orderId)
            fetchOrders(userId)
        }
    }

    fun updateOrder(userId: String, order: Order) {
        viewModelScope.launch {
            repository.updateOrder(userId, order)
            fetchOrders(userId)
        }
    }
}