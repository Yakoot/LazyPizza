package dev.mamkin.lazypizza.order.presentation.cart

sealed interface CartAction {
    data class DeleteClick(val id: String) : CartAction
    data class PlusClick(val id: String) : CartAction
    data class MinusClick(val id: String) : CartAction
}