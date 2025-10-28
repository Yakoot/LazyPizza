package dev.mamkin.lazypizza.order.presentation.productDetails

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import coil.compose.AsyncImage
import dev.mamkin.lazypizza.R
import dev.mamkin.lazypizza.core.presentation.designsystem.buttons.FilledButton
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import dev.mamkin.lazypizza.order.domain.models.MenuItem
import dev.mamkin.lazypizza.order.presentation.components.ToppingCard
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProductDetailsRoot(
    pizza: String,
    navigateBack: () -> Unit,
    viewModel: ProductDetailsViewModel = koinViewModel(key = pizza) { parametersOf(pizza) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest {
            when (it) {
                ProductDetailsScreenEvent.NavigateBack -> navigateBack()
            }
        }
    }

    ProductDetailsScreen(
        state = state,
        onAction = {
            when (it) {
                ProductDetailsAction.NavigateBack -> navigateBack()
                else -> viewModel.onAction(it)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    state: ProductDetailsState,
    onAction: (ProductDetailsAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isWideScreen = windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.padding(start = 10.dp),
                        onClick = { onAction(ProductDetailsAction.NavigateBack) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = AppTheme.colors.textSecondary8,
                            contentColor = AppTheme.colors.textSecondary
                        ),
                        shape = CircleShape
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_left),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colors.bg
                )
            )
        },
    ) {
        when (state) {
            is ProductDetailsState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .wrapContentSize(),
                    color = AppTheme.colors.primary
                )
            }

            is ProductDetailsState.Success -> {
                if (isWideScreen) {
                    HorizontalLayout(
                        modifier = Modifier.padding(it),
                        leftContent = {
                            Column() {
                                PizzaImage(data = state.pizza)
                                Spacer(modifier = Modifier.height(16.dp))
                                PizzaInfo(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    data = state.pizza
                                )
                            }
                        },
                        rightContent = {
                            Toppings(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                toppings = state.toppings,
                                onAction = onAction
                            )
                            ButtonView(
                                price = state.totalPrice,
                                onClick = {
                                    onAction(ProductDetailsAction.AddToCart)
                                }
                            )
                        }
                    )
                } else {
                    VerticalLayout(
                        modifier = Modifier.padding(it),
                        topContent = {
                            PizzaImage(data = state.pizza)
                        },
                        bottomContent = {
                            Column(
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 20.dp
                                )
                            ) {
                                PizzaInfo(data = state.pizza)
                                Spacer(modifier = Modifier.height(16.dp))
                                Toppings(toppings = state.toppings, onAction = onAction)
                            }
                            ButtonView(
                                price = state.totalPrice,
                                onClick = {
                                    onAction(ProductDetailsAction.AddToCart)
                                }
                            )
                        }
                    )
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
fun PizzaInfo(
    modifier: Modifier = Modifier,
    data: MenuItem.Pizza
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = data.title,
            style = AppTheme.typography.title1SemiBold,
            color = AppTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = data.ingredients,
            style = AppTheme.typography.body3Regular,
            color = AppTheme.colors.textSecondary,
        )
    }
}

@Composable
fun PizzaImage(
    modifier: Modifier = Modifier,
    data: MenuItem.Pizza
) {
    AsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .size(240.dp),
        model = data.image,
        contentDescription = data.title,
        placeholder = BrushPainter(
            Brush.linearGradient(
                listOf(
                    Color(color = 0xFFFFFFFF),
                    Color(color = 0xFFDDDDDD),
                )
            )
        ),
    )
}

@Composable
fun Toppings(
    modifier: Modifier = Modifier,
    toppings: List<ToppingUi>,
    onAction: (ProductDetailsAction) -> Unit
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(
                id = R.string.add_extra_topping,
            ).uppercase(),
            style = AppTheme.typography.label2SemiBold,
            color = AppTheme.colors.textSecondary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(bottom = 50.dp)
        ) {
            items(toppings) {
                ToppingCard(
                    data = it,
                    onClick = {
                        onAction(ProductDetailsAction.ToppingClick(it.id))
                    },
                    onPlusClick = {
                        onAction(ProductDetailsAction.AddTopping(it.id))
                    },
                    onMinusClick = {
                        onAction(ProductDetailsAction.RemoveTopping(it.id))
                    }
                )
            }
        }
    }

}

@Composable
private fun BoxScope.ButtonView(
    modifier: Modifier = Modifier,
    price: Double = 0.0,
    onClick: () -> Unit = {}
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
            text = stringResource(R.string.add_to_cart_button_text, price),
            onClick = onClick
        )
    }
}

@Preview(name = "Tablet - Landscape", device = TABLET, showSystemUi = true)
@Composable
private fun Preview() {
    LazyPizzaTheme {
        ProductDetailsScreen(
            state = ProductDetailsState.Success(
                totalPrice = 12.99,
                pizza = MenuItem.Pizza(
                    id = "1",
                    title = "Four cheese",
                    price = 10.0,
                    image = "",
                    ingredients = "Cheese 1, Cheese 2, Cheese 3, Cheese 4"
                ),
                toppings = listOf(
                    ToppingUi(
                        id = "1",
                        title = "Topping 1",
                        image = "",
                        price = 1.0,
                        count = 0
                    ),
                    ToppingUi(
                        id = "1",
                        title = "Topping 2",
                        image = "",
                        price = 13.0,
                        count = 0
                    ),
                    ToppingUi(
                        id = "1",
                        title = "Topping 3",
                        image = "",
                        price = 14.0,
                        count = 2
                    ),
                    ToppingUi(
                        id = "1",
                        title = "Topping 4",
                        image = "",
                        price = 15.0,
                        count = 0
                    )
                )
            ),
            onAction = {}
        )
    }
}
