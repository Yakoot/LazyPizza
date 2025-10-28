package dev.mamkin.lazypizza.order.presentation.home

sealed interface HomeScreenEvent {
    data class ShowSnackbar(val message: String) : HomeScreenEvent
}
