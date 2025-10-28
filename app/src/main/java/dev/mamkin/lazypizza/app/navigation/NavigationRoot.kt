package dev.mamkin.lazypizza.app.navigation

import android.R.attr.text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import dev.mamkin.lazypizza.R
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.order.domain.CartRepository
import dev.mamkin.lazypizza.order.presentation.cart.CartRoot
import dev.mamkin.lazypizza.order.presentation.home.HomeRoot
import dev.mamkin.lazypizza.order.presentation.productDetails.ProductDetailsRoot
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Menu)
    val itemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = AppTheme.colors.primary,
            selectedTextColor = AppTheme.colors.textPrimary,
            indicatorColor = AppTheme.colors.primary8,
            unselectedIconColor = AppTheme.colors.textSecondary,
            unselectedTextColor = AppTheme.colors.textSecondary,
            disabledIconColor = AppTheme.colors.textSecondary,
            disabledTextColor = AppTheme.colors.textSecondary,
        )
    )

    val cartRepository: CartRepository = koinInject()
    val cartItemsCount by cartRepository.cartItemsCount.collectAsStateWithLifecycle(0)
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            TOP_LEVEL_ROUTES.forEach { route ->
                val isSelected = route == backStack.lastOrNull()
                item(
                    label = {
                        Text(
                            text = route.label,
                            style = AppTheme.typography.title4
                        )
                    },
                    icon = {
                        Box() {
                            Icon(
                                painter = painterResource(id = route.icon),
                                contentDescription = route.label
                            )
                            if (route == Cart && cartItemsCount > 0) {
                                Badge(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = 6.dp, y = (-6).dp)
                                    ,
                                    containerColor = AppTheme.colors.primary,
                                    contentColor = AppTheme.colors.textOnPrimary
                                ) {
                                    Text(
                                        text = cartItemsCount.toString(),
                                        style = AppTheme.typography.title4
                                    )
                                }
                            }
                        }
                    },
                    selected = isSelected,
                    onClick = {
                        backStack.apply {
                            clear()
                            add(route as NavKey)
                            backStack.forEach {
                                println(it)
                            }
                        }
                    },
                    colors = itemColors
                )
            }
        },
    ) {
        NavDisplay(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainerLowest),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = { key ->
                when (key) {
                    is Menu -> NavEntry(key) {
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

                    is Cart -> NavEntry(key) {
                        CartRoot()
                    }

                    is History -> NavEntry(key) {}

                    else -> throw IllegalArgumentException("Unknown key: $key")
                }
            }
        )
    }

}

private sealed interface TopLevelRoute {
    val icon: Int
    val label: String
}

@Serializable
data object Menu : NavKey, TopLevelRoute {
    override val icon: Int = R.drawable.menu
    override val label: String = "Menu"
}

@Serializable
data object Cart : NavKey, TopLevelRoute {
    override val icon: Int = R.drawable.cart
    override val label: String = "Cart"
}

@Serializable
data object History : NavKey, TopLevelRoute {
    override val icon: Int = R.drawable.history
    override val label: String = "History"
}

@Serializable
data class ProductDetails(
    val pizza: String
) : NavKey

private val TOP_LEVEL_ROUTES: List<TopLevelRoute> = listOf(Menu, Cart, History)


class TopLevelBackStack<T : Any>(startKey: T) {

    // Maintain a stack for each top level route
    private var topLevelStacks: LinkedHashMap<T, SnapshotStateList<T>> = linkedMapOf(
        startKey to mutableStateListOf(startKey)
    )

    // Expose the current top level route for consumers
    var topLevelKey by mutableStateOf(startKey)
        private set

    // Expose the back stack so it can be rendered by the NavDisplay
    val backStack = mutableStateListOf(startKey)

    private fun updateBackStack() =
        backStack.apply {
            clear()
            addAll(topLevelStacks.flatMap { it.value })
        }

    fun addTopLevel(key: T) {

        // If the top level doesn't exist, add it
        if (topLevelStacks[key] == null) {
            topLevelStacks.put(key, mutableStateListOf(key))
        } else {
            // Otherwise just move it to the end of the stacks
            topLevelStacks.apply {
                remove(key)?.let {
                    put(key, it)
                }
            }
        }
        topLevelKey = key
        updateBackStack()
    }

    fun add(key: T) {
        topLevelStacks[topLevelKey]?.add(key)
        updateBackStack()
    }

    fun removeLast() {
        val removedKey = topLevelStacks[topLevelKey]?.removeLastOrNull()
        // If the removed key was a top level key, remove the associated top level stack
        topLevelStacks.remove(removedKey)
        topLevelKey = topLevelStacks.keys.last()
        updateBackStack()
    }
}
