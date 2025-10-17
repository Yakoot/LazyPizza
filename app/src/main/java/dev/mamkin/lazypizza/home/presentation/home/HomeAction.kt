package dev.mamkin.lazypizza.home.presentation.home

sealed interface HomeAction {
    data class PizzaClick(val pizza: String) : HomeAction
    data class SearchInput(val value: String) : HomeAction
    data class AddClick(val id: String) : HomeAction
    data class DeleteClick(val id: String) : HomeAction
    data class PlusClick(val id: String) : HomeAction
    data class MinusClick(val id: String) : HomeAction
}
