package dev.mamkin.lazypizza.home.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Drink(
    val id: String = "",
    val title: String = "",
    val price: Double = 0.0,
    val image: String = ""
)
