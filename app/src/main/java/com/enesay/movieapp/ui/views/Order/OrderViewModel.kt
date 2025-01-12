package com.enesay.movieapp.ui.views.Order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enesay.movieapp.data.model.Order
import com.enesay.movieapp.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(val repository: PaymentRepository) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> get() = _orders

    fun fetchOrders(userId: String) {
        viewModelScope.launch {
            Log.d("orders vm", "orders vm")
            _orders.value = repository.getOrders(userId)
        }
    }
}