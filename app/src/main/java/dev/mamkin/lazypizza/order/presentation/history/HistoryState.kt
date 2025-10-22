package dev.mamkin.lazypizza.order.presentation.history

data class HistoryState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)