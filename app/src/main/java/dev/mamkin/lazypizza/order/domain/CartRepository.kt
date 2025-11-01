package dev.mamkin.lazypizza.order.domain

import dev.mamkin.lazypizza.order.domain.models.cart.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    val cart: Flow<List<CartItem>>

    val cartItemsCount: Flow<Int>

    suspend fun addItem(item: CartItem)

    suspend fun removeItem(cartItemId: String)
    suspend fun removeItemByProductId(productId: String)

    suspend fun decreaseItemByProductId(productId: String)

    suspend fun increaseItemByProductId(productId: String)

    suspend fun decreaseItemById(id: String)

    suspend fun increaseItemById(id: String)

    suspend fun updateItemQuantity(cartItemId: String, newQuantity: Int)

    suspend fun clearCart()
}