package dev.mamkin.lazypizza.order.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Pizza(
    val id: String = "",
    val title: String = "",
    val price: Double = 0.0,
    val image: String = "",
    val ingredients: String = "",
)
