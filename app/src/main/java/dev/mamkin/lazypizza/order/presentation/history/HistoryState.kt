package dev.mamkin.lazypizza.order.presentation.history

import androidx.compose.runtime.Stable
import dev.mamkin.lazypizza.order.presentation.history.components.OrderHistoryCardUi

@Stable
sealed interface HistoryState {
    data object NotLoggedIn : HistoryState
    data class LoggedIn(
        val items: List<OrderHistoryCardUi> = emptyList(),
    ) : HistoryState
}
