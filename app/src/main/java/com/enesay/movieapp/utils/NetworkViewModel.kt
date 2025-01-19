package com.enesay.movieapp.utils

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val networkMonitorHelper: NetworkMonitorHelper
) : ViewModel() {
    val isConnected: StateFlow<Boolean> = networkMonitorHelper.isConnected

    init {
        networkMonitorHelper.startMonitoring()
    }

    override fun onCleared() {
        super.onCleared()
        networkMonitorHelper.stopMonitoring()
    }

    fun retryConnectionStatus(){
        networkMonitorHelper.startMonitoring()
    }
}
