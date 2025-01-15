package com.enesay.movieapp.ui.views.AddCard

data class AddCardUiState(
    val cardNumber: String = "",
    val cardNumberError: String? = null,
    val cardHolderName: String = "",
    val cardHolderNameError: String? = null,
    val expiryDate: String = "",
    val expiryDateError: String? = null,
    val cvv: String = "",
    val cvvError: String? = null,
    val isSaved: Boolean = false
) {
    val isFormValid: Boolean
        get() = cardNumberError == null && cardHolderNameError == null &&
                expiryDateError == null && cvvError == null &&
                cardNumber.isNotBlank() && cardHolderName.isNotBlank() &&
                expiryDate.isNotBlank() && cvv.isNotBlank()
}