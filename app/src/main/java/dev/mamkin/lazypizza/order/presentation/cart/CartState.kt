package dev.mamkin.lazypizza.order.presentation.cart


sealed interface CartState {
    data object Empty : CartState
    data object Loading : CartState
    data class Content(
        val totalPrice: Double,
    ) : CartState
}