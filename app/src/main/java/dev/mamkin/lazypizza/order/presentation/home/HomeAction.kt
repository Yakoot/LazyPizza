package dev.mamkin.lazypizza.order.presentation.home

import dev.mamkin.lazypizza.order.presentation.models.ProductCardUi

sealed interface HomeAction {
    data class PizzaClick(val pizza: String) : HomeAction
    data class SearchInput(val value: String) : HomeAction
    data class AddClick(val item: ProductCardUi) : HomeAction
    data class DeleteClick(val id: String) : HomeAction
    data class PlusClick(val id: String) : HomeAction
    data class MinusClick(val id: String) : HomeAction
}
