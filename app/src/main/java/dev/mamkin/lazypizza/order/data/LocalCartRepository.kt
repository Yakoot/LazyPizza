package dev.mamkin.lazypizza.order.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.mamkin.lazypizza.app.cartDataStore
import dev.mamkin.lazypizza.order.domain.CartRepository
import dev.mamkin.lazypizza.order.domain.models.cart.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class LocalCartRepository(
    private val context: Context,
) : CartRepository {
    private val cartKey = stringPreferencesKey("cart_data")

    override val cart: Flow<List<CartItem>> = context.cartDataStore.data
        .map { preferences ->
            preferences[cartKey]?.takeIf { it.isNotBlank() }?.let {
                Json.decodeFromString<List<CartItem>>(it)
            } ?: emptyList()
        }

    override val cartItemsCount: Flow<Int> = cart.map { items ->
        items.sumOf { it.quantity }
    }


    override suspend fun addItem(item: CartItem) {
        val currentCart = cart.first().toMutableList()
        currentCart.add(item)
        updateDataStore(currentCart)
    }

    override suspend fun removeItem(cartItemId: String) {
        val currentCart = cart.first().toMutableList()
        currentCart.removeAll { it.id == cartItemId }
        updateDataStore(currentCart)
    }

    override suspend fun removeItemByProductId(productId: String) {
        val currentCart = cart.first().toMutableList()
        currentCart.removeAll { it.productId == productId }
        updateDataStore(currentCart)
    }

    override suspend fun decreaseItemByProductId(productId: String) {
        val currentCart = cart.first().toMutableList()
        val item = currentCart.find { it.productId == productId }
        if (item != null) {
            updateItemQuantity(item.id, item.quantity - 1)
        }
    }

    override suspend fun increaseItemByProductId(productId: String) {
        val currentCart = cart.first().toMutableList()
        val item = currentCart.find { it.productId == productId }
        if (item != null) {
            updateItemQuantity(item.id, item.quantity + 1)
        }
    }

    override suspend fun increaseItemById(id: String) {
        val currentCart = cart.first().toMutableList()
        val item = currentCart.find { it.id == id }
        if (item != null) {
            updateItemQuantity(item.id, item.quantity + 1)
        }
    }

    override suspend fun decreaseItemById(id: String) {
        val currentCart = cart.first().toMutableList()
        val item = currentCart.find { it.id == id }
        if (item != null) {
            updateItemQuantity(item.id, item.quantity - 1)
        }
    }

    override suspend fun updateItemQuantity(cartItemId: String, newQuantity: Int) {
        val currentCart = cart.first().toMutableList()
        val itemIndex = currentCart.indexOfFirst { it.id == cartItemId }

        if (itemIndex == -1) return // Элемент не найден

        if (newQuantity <= 0) {
            // Если новое количество 0 или меньше, удаляем элемент
            currentCart.removeAt(itemIndex)
        } else {
            // В противном случае, обновляем его
            val currentItem = currentCart[itemIndex]
            val updatedItem = when (currentItem) {
                is CartItem.Pizza -> currentItem.copy(quantity = newQuantity)
                is CartItem.Other -> currentItem.copy(quantity = newQuantity)
            }
            currentCart[itemIndex] = updatedItem
        }

        updateDataStore(currentCart)
    }


    override suspend fun clearCart() {
        updateDataStore(emptyList())
    }

    private suspend fun updateDataStore(items: List<CartItem>) {
        context.cartDataStore.edit { preferences ->
            preferences[cartKey] = Json.encodeToString(items)
        }
    }
}