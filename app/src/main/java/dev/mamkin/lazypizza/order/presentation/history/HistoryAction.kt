package dev.mamkin.lazypizza.order.presentation.history

sealed interface HistoryAction {
    data object GoToSignIn : HistoryAction
}