package dev.mamkin.lazypizza.app.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import dev.mamkin.lazypizza.home.domain.models.Pizza
import dev.mamkin.lazypizza.home.presentation.home.HomeRoot
import dev.mamkin.lazypizza.home.presentation.productDetails.ProductDetailsRoot
import kotlinx.serialization.Serializable

@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Home)
    NavDisplay(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainerLowest),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Home -> NavEntry(key) {
                    HomeRoot(
                        navigateToDetails = { pizza ->
                            backStack.add(ProductDetails(pizza))
                        }
                    )
                }

                is ProductDetails -> NavEntry(key) {
                    ProductDetailsRoot(
                        pizza = key.pizza,
                        navigateBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                else -> throw IllegalArgumentException("Unknown key: $key")
            }
        }
    )
}

@Serializable
data object Home: NavKey

@Serializable
data class ProductDetails(
    val pizza: String
): NavKey