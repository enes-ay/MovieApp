package com.enesay.movieapp.ui.views.AddCard

sealed class AddCardEvent {
    data class CardNumberChanged(val cardNumber: String) : AddCardEvent()
    data class CardHolderNameChanged(val cardHolderName: String) : AddCardEvent()
    data class ExpiryDateChanged(val expiryDate: String) : AddCardEvent()
    data class CvvChanged(val cvv: String) : AddCardEvent()
    object SaveCard : AddCardEvent()
}