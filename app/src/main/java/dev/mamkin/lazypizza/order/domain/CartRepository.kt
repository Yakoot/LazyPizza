package dev.mamkin.lazypizza.order.domain

import kotlinx.coroutines.flow.Flow

interface CartRepository {
    val cart: Flow<Map<String, Int>>
    val cartItemsCount: Flow<Int>
    suspend fun updateCart(cart: Map<String, Int>)
    suspend fun addToCart(productId: String)
    suspend fun removeFromCart(productId: String)
    suspend fun clearCart()
}