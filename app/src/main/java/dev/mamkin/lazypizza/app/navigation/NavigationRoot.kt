package dev.mamkin.lazypizza.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import dev.mamkin.lazypizza.R
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.order.domain.CartRepository
import dev.mamkin.lazypizza.order.presentation.cart.CartRoot
import dev.mamkin.lazypizza.order.presentation.history.HistoryRoot
import dev.mamkin.lazypizza.order.presentation.home.HomeRoot
import dev.mamkin.lazypizza.order.presentation.productDetails.ProductDetailsRoot
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Menu)
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isWideScreen = windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND)

    val cartRepository: CartRepository = koinInject()
    val cartItemsCount by cartRepository.cartItemsCount.collectAsStateWithLifecycle(0)

    val navigationBarItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = AppTheme.colors.primary,
        selectedTextColor = AppTheme.colors.textPrimary,
        indicatorColor = AppTheme.colors.primary8,
        unselectedIconColor = AppTheme.colors.textSecondary,
        unselectedTextColor = AppTheme.colors.textSecondary,
        disabledIconColor = AppTheme.colors.textSecondary,
        disabledTextColor = AppTheme.colors.textSecondary,
    )

    val navigationRailItemColors = NavigationRailItemDefaults.colors(
        selectedIconColor = AppTheme.colors.primary,
        selectedTextColor = AppTheme.colors.textPrimary,
        indicatorColor = AppTheme.colors.primary8,
        unselectedIconColor = AppTheme.colors.textSecondary,
        unselectedTextColor = AppTheme.colors.textSecondary,
        disabledIconColor = AppTheme.colors.textSecondary,
        disabledTextColor = AppTheme.colors.textSecondary,
    )

    Scaffold(
        bottomBar = {
            if (!isWideScreen) {
                Row(
                    modifier = Modifier
                        .shadow(elevation = 16.dp)
                        .fillMaxWidth()
                        .background(AppTheme.colors.surfaceHigher),
                    horizontalArrangement = Arrangement.Center
                ) {
                    NavigationBar(
                        containerColor = AppTheme.colors.surfaceHigher,
                        modifier = Modifier.width(304.dp)
                    ) {
                        TOP_LEVEL_ROUTES.forEach { route ->
                            val isSelected = route == backStack.lastOrNull()
                            NavigationBarItem(
                                icon = {
                                    NavigationIcon(
                                        route = route,
                                        cartItemsCount = cartItemsCount
                                    )
                                },
                                label = {
                                    Text(
                                        text = route.label,
                                        style = AppTheme.typography.title4
                                    )
                                },
                                selected = isSelected,
                                onClick = {
                                    backStack.apply {
                                        clear()
                                        add(route as NavKey)
                                    }
                                },
                                colors = navigationBarItemColors
                            )
                        }
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        if (isWideScreen) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NavigationRail(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
                    ) {
                        TOP_LEVEL_ROUTES.forEach { route ->
                            val isSelected = route == backStack.lastOrNull()
                            NavigationRailItem(
                                icon = {
                                    NavigationIcon(
                                        route = route,
                                        cartItemsCount = cartItemsCount
                                    )
                                },
                                label = {
                                    Text(
                                        text = route.label,
                                        style = AppTheme.typography.title4
                                    )
                                },
                                selected = isSelected,
                                onClick = {
                                    backStack.apply {
                                        clear()
                                        add(route as NavKey)
                                    }
                                },
                                colors = navigationRailItemColors
                            )
                        }
                    }
                }
                AppNavDisplay(
                    modifier = modifier
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest),
                    backStack = backStack
                )
            }
        } else {
            AppNavDisplay(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest),
                backStack = backStack
            )
        }
    }
}

@Composable
private fun AppNavDisplay(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<NavKey>,
) {
    NavDisplay(
        modifier = modifier,
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
                    CartRoot(
                        backToMenu = {
                            backStack.apply {
                                clear()
                                add(Menu)
                            }
                        }
                    )
                }

                is History -> NavEntry(key) {
                    HistoryRoot()
                }

                else -> throw IllegalArgumentException("Unknown key: $key")
            }
        }
    )
}

@Composable
private fun NavigationIcon(
    route: TopLevelRoute,
    cartItemsCount: Int,
) {
    Box {
        Icon(
            painter = painterResource(id = route.icon),
            contentDescription = route.label
        )
        if (route == Cart && cartItemsCount > 0) {
            Badge(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 12.dp, y = (-12).dp),
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
