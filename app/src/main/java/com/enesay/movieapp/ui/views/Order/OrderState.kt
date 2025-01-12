package com.enesay.movieapp.ui.views.Order

sealed class OrderState {
    object Idle : OrderState() // No operation in progress
    object Loading : OrderState() // Order creation in progress
    object Success : OrderState() // Order created successfully
    data class Error(val message: String) : OrderState() // Error during order creation
}
