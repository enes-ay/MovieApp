package com.enesay.movieapp.ui.views.AddCard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enesay.movieapp.data.model.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AddCardUiState())
    val uiState: StateFlow<AddCardUiState> = _uiState

    fun onEvent(event: AddCardEvent) {
        when (event) {
            is AddCardEvent.CardNumberChanged -> {
                _uiState.update {
                    it.copy(
                        cardNumber = event.cardNumber,
                        cardNumberError = if (event.cardNumber.length < 16) "Card number must be 16 digits" else null
                    )
                }
            }
            is AddCardEvent.CardHolderNameChanged -> {
                _uiState.update {
                    it.copy(
                        cardHolderName = event.cardHolderName,
                        cardHolderNameError = if (event.cardHolderName.isEmpty()) "Name cannot be empty" else null
                    )
                }
            }
            is AddCardEvent.ExpiryDateChanged -> {
                _uiState.update {
                    it.copy(
                        expiryDate = event.expiryDate,
                        expiryDateError = if (event.expiryDate.length < 4) "Invalid expiry date" else null
                    )
                }
            }
            is AddCardEvent.CvvChanged -> {
                _uiState.update {
                    it.copy(
                        cvv = event.cvv,
                        cvvError = if (event.cvv.length < 3) "CVV must be 3 digits" else null
                    )
                }
            }
            AddCardEvent.SaveCard -> {
                if (_uiState.value.isFormValid) {
                    _uiState.update { it.copy(isSaved = true) }
                }
            }
        }
    }
}
