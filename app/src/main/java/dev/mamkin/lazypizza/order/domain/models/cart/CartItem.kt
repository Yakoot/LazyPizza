@file:OptIn(ExperimentalUuidApi::class)

package dev.mamkin.lazypizza.order.domain.models.cart

import dev.mamkin.lazypizza.order.domain.models.ProductType
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
sealed interface CartItem {
    val id: String
    val productId: String
    val quantity: Int

    val price: Double

    @Serializable
    data class Pizza(
        override val id: String = Uuid.random().toString(),
        override val productId: String,
        override val quantity: Int,
        override val price: Double,
        val toppings: List<ToppingItem>
    ) : CartItem

    @Serializable
    data class Other(
        override val id: String = Uuid.random().toString(),
        override val productId: String,
        override val quantity: Int,
        override val price: Double,
        val productType: ProductType
    ) : CartItem
}

@Serializable
data class ToppingItem(
    val id: String = Uuid.random().toString(),
    val productId: String,
    val quantity: Int
)