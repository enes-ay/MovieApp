package com.enesay.movieapp.ui.views.Payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enesay.movieapp.data.model.Card
import com.enesay.movieapp.data.model.MovieCart
import com.enesay.movieapp.data.model.Order
import com.enesay.movieapp.data.repository.PaymentRepository
import com.enesay.movieapp.domain.usecase.DeleteAllItemsUseCase
import com.enesay.movieapp.ui.views.Cart.CartViewModel
import com.enesay.movieapp.ui.views.Order.OrderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(private val repository: PaymentRepository,
    private val deleteAllItemsUseCase: DeleteAllItemsUseCase) :
    ViewModel() {
    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> get() = _cards

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> get() = _orders

    private val _orderState = MutableStateFlow<OrderState>(OrderState.Idle)
    val orderState: StateFlow<OrderState> get() = _orderState


    fun fetchOrders(userId: String) {
        viewModelScope.launch {
            _orders.value = repository.getOrders(userId)
        }
    }

    fun fetchCards(userId: String) {
        viewModelScope.launch {
            _cards.value = repository.getCards(userId)
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
            _orderState.value = OrderState.Loading
            try {
                repository.addOrder(userId, order) // Add the order to Firestore
                //cartViewModel.clearCart() // Clear the cart after order creation
                _orderState.value = OrderState.Success
            } catch (e: Exception) {
                _orderState.value = OrderState.Error(e.localizedMessage ?: "An error occurred")
            }
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

    fun deleteAllCartItems(movieList: List<MovieCart>){
        viewModelScope.launch {
            try {
                deleteAllItemsUseCase.deleteAllItemsCart(movieList = movieList, "enes_ay")
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}