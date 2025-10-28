package dev.mamkin.lazypizza.order.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import dev.mamkin.lazypizza.R
import dev.mamkin.lazypizza.core.presentation.designsystem.buttons.FilledButton
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import dev.mamkin.lazypizza.order.domain.models.ProductType
import dev.mamkin.lazypizza.order.presentation.cart.components.EmptyCart
import dev.mamkin.lazypizza.order.presentation.cart.components.RecommendedCard
import dev.mamkin.lazypizza.order.presentation.components.ProductCard
import dev.mamkin.lazypizza.order.presentation.models.ProductCardUi
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartRoot(
    viewModel: CartViewModel = koinViewModel(),
    backToMenu: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CartScreen(
        state = state,
        onAction = {
            println("!!!!! onAction $it")
            when (it) {
                is CartAction.BackToMenuClick -> backToMenu()
                else -> viewModel.onAction(it)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    state: CartState,
    onAction: (CartAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isWideScreen = windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.cart_page_topbar_title),
                        style = AppTheme.typography.body1Medium,
                        color = AppTheme.colors.textPrimary
                    )
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.TopCenter
        ) {
            when (state) {
                CartState.Empty -> EmptyCart(
                    modifier = Modifier.padding(top = 120.dp, start = 16.dp, end = 16.dp),
                    onClick = {
                        onAction(CartAction.BackToMenuClick)
                    }
                )

                CartState.Loading -> CircularProgressIndicator(
                    color = AppTheme.colors.primary,
                    modifier = Modifier.align(Alignment.Center)
                )

                is CartState.Content -> {
                    if (isWideScreen) {
                        HorizontalLayout(
                            leftContent = {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 100.dp
                                    )
                                ) {
                                    items(state.items) {
                                        ProductCard(
                                            data = it,
                                            onDeleteClick = {
                                                onAction(CartAction.DeleteClick(it.id))
                                            },
                                            onPlusClick = {
                                                onAction(CartAction.PlusClick(it.id))
                                            },
                                            onMinusClick = {
                                                onAction(CartAction.MinusClick(it.id))
                                            }
                                        )
                                    }
                                    if (state.recommended.isNotEmpty()) {
                                        item {
                                            Spacer(modifier = Modifier.height(12.dp))
                                            Text(
                                                text = "Recommended to add to your order".toUpperCase(
                                                    Locale.current
                                                ),
                                                style = AppTheme.typography.label2SemiBold,
                                                color = AppTheme.colors.textSecondary
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            LazyRow(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                items(state.recommended) {
                                                    RecommendedCard(
                                                        data = it,
                                                        onClickAdd = {
                                                            onAction(
                                                                CartAction.AddRecommendedClick(
                                                                    it
                                                                )
                                                            )
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        ) { }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 100.dp
                            )
                        ) {
                            items(state.items) {
                                ProductCard(
                                    data = it,
                                    onDeleteClick = {
                                        onAction(CartAction.DeleteClick(it.id))
                                    },
                                    onPlusClick = {
                                        onAction(CartAction.PlusClick(it.id))
                                    },
                                    onMinusClick = {
                                        onAction(CartAction.MinusClick(it.id))
                                    }
                                )
                            }
                            if (state.recommended.isNotEmpty()) {
                                item {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Recommended to add to your order".toUpperCase(Locale.current),
                                        style = AppTheme.typography.label2SemiBold,
                                        color = AppTheme.colors.textSecondary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(state.recommended) {
                                            RecommendedCard(
                                                data = it,
                                                onClickAdd = {
                                                    onAction(CartAction.AddRecommendedClick(it))
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                        }
                        ButtonView(
                            text = state.buttonText,
                            onClick = {}
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun VerticalLayout(
    modifier: Modifier = Modifier,
    topContent: @Composable () -> Unit,
    bottomContent: @Composable BoxScope.() -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(AppTheme.colors.surfaceHigher)
                .clip(
                    RoundedCornerShape(bottomEnd = 16.dp)
                )
                .background(AppTheme.colors.bg)
        ) {
            topContent()
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(AppTheme.colors.bg)
                .dropShadow(
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    shadow = Shadow(4.dp, color = Color(0x0A03131F))
                )
                .clip(
                    RoundedCornerShape(topStart = 16.dp)
                )
                .background(AppTheme.colors.surfaceHigher)
        ) {
            bottomContent()
        }
    }
}

@Composable
fun HorizontalLayout(
    modifier: Modifier = Modifier,
    leftContent: @Composable () -> Unit,
    rightContent: @Composable BoxScope.() -> Unit,
) {
    Row(
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.weight(1f)) {
            leftContent()
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .dropShadow(
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    shadow = Shadow(4.dp, color = Color(0x0A03131F))
                )
                .clip(
                    RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                )
                .background(AppTheme.colors.surfaceHigher)
        ) {
            rightContent()
        }
    }
}

@Composable
private fun BoxScope.ButtonView(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0f),
                        Color.White.copy(alpha = 1f),
                    )
                )
            )
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.BottomCenter

    ) {
        FilledButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            text = text,
            onClick = onClick
        )
    }
}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        CartScreen(
            state = CartState.Content(
                buttonText = "Proceed to Checkout ()",
                items = listOf(
                    ProductCardUi(
                        title = "Four cheese",
                        priceText = "$12.99",
                        price = 12.99,
                        count = 4,
                        priceCalculation = "23324234",
                        totalPriceText = "23324234",
                        image = "",
                        type = ProductType.PIZZA,
                        showAddButton = false
                    ),
                    ProductCardUi(
                        title = "Four cheese",
                        priceText = "$12.99",
                        price = 12.99,
                        count = 4,
                        priceCalculation = "23324234",
                        totalPriceText = "23324234",
                        image = "",
                        type = ProductType.PIZZA,
                        showAddButton = false
                    ),
                    ProductCardUi(
                        title = "Four cheese",
                        priceText = "$12.99",
                        price = 12.99,
                        count = 4,
                        priceCalculation = "23324234",
                        totalPriceText = "23324234",
                        image = "",
                        type = ProductType.PIZZA,
                        showAddButton = false
                    ),
                    ProductCardUi(
                        title = "Four cheese",
                        priceText = "$12.99",
                        price = 12.99,
                        count = 4,
                        priceCalculation = "23324234",
                        totalPriceText = "23324234",
                        image = "",
                        type = ProductType.PIZZA,
                        showAddButton = false
                    ),
                    ProductCardUi(
                        title = "Four cheese",
                        priceText = "$12.99",
                        description = "1 x Extra cheese\n1 x Extra cheese\n1 x Extra cheese",
                        price = 12.99,
                        count = 4,
                        priceCalculation = "23324234",
                        totalPriceText = "23324234",
                        image = "",
                        type = ProductType.PIZZA,
                        showAddButton = false
                    )
                ),
                recommended = listOf(

                )
            ),
            onAction = {}
        )
    }
}