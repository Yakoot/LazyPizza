package dev.mamkin.lazypizza.order.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.mamkin.lazypizza.app.cartDataStore
import dev.mamkin.lazypizza.order.domain.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class LocalCartRepository(
    private val context: Context,
    private val json: Json,
) : CartRepository {
    private val cartKey = stringPreferencesKey("cart_data")

    override val cart: Flow<Map<String, Int>> = context.cartDataStore.data
        .map { preferences ->
            preferences[cartKey]?.let {
                json.decodeFromString<Map<String, Int>>(it)
            } ?: emptyMap()
        }
    override val cartItemsCount: Flow<Int> = cart.map { it.values.sum() }

    override suspend fun updateCart(cart: Map<String, Int>) {
        context.cartDataStore.edit { preferences ->
            preferences[cartKey] = json.encodeToString(cart)
        }
    }

    override suspend fun addToCart(productId: String) {
        context.cartDataStore.edit { preferences ->
            val currentCart = preferences[cartKey]?.let {
                json.decodeFromString<Map<String, Int>>(it)
            } ?: emptyMap()
            val updatedCart = currentCart.toMutableMap()
            updatedCart[productId] = (updatedCart[productId] ?: 0) + 1
            preferences[cartKey] = json.encodeToString(updatedCart.toMap())
        }
    }

    override suspend fun removeFromCart(productId: String) {
        context.cartDataStore.edit { preferences ->
            val currentCart = preferences[cartKey]?.let {
                json.decodeFromString<Map<String, Int>>(it)
            } ?: emptyMap()
            val updatedCart = currentCart.toMutableMap()
            val currentCount = updatedCart[productId] ?: 0
            if (currentCount > 1) {
                updatedCart[productId] = currentCount - 1
            } else {
                updatedCart.remove(productId)
            }

            preferences[cartKey] = json.encodeToString(updatedCart.toMap())
        }
    }

    override suspend fun clearCart() {
        context.cartDataStore.edit { preferences ->
            preferences[cartKey] = ""
        }
    }
}