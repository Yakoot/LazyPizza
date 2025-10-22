package dev.mamkin.lazypizza.order.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class CartViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow<CartState>(CartState.Loading)
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                loadData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CartState.Loading
        )

    private fun loadData() {
        _state.value = CartState.Empty
    }

    fun onAction(action: CartAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}