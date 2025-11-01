package dev.mamkin.lazypizza.order.presentation.history

sealed interface HistoryState {
    data object NotLoggedIn : HistoryState
}
