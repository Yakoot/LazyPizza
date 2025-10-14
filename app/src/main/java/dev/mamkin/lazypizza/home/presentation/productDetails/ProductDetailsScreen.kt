package dev.mamkin.lazypizza.home.presentation.productDetails

import android.R.attr.onClick
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dev.mamkin.lazypizza.R
import dev.mamkin.lazypizza.core.presentation.designsystem.buttons.FilledButton
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import dev.mamkin.lazypizza.home.domain.models.Pizza
import dev.mamkin.lazypizza.home.presentation.components.ToppingCard
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProductDetailsRoot(
    pizza: String,
    navigateBack: () -> Unit,
    viewModel: ProductDetailsViewModel = koinViewModel(key = pizza) { parametersOf(pizza) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(
        Unit
    ) {
        println("!!!!! ${viewModel.toString()}")
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
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
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            when (state) {
                is ProductDetailsState.Success -> {
                    Box(
                        modifier = Modifier
                            .height(240.dp)
                            .fillMaxWidth()
                            .background(AppTheme.colors.surfaceHigher)
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(bottomEnd = 16.dp),
                            color = AppTheme.colors.bg
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                                ,
                                model = state.pizza.image,
                                contentDescription = null,
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
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(AppTheme.colors.bg)
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize().dropShadow(
                                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                                shadow = Shadow(4.dp, color = Color(0x0A03131F))
                            ),
                            shape = RoundedCornerShape(topStart = 16.dp),
                            color = AppTheme.colors.surfaceHigher,
                            shadowElevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 20.dp
                                )
                            ) {
                                Text(
                                    text = state.pizza.title,
                                    style = AppTheme.typography.title1SemiBold,
                                    color = AppTheme.colors.textPrimary,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = state.pizza.ingredients,
                                    style = AppTheme.typography.body3Regular,
                                    color = AppTheme.colors.textSecondary,
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                LazyVerticalGrid(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    columns = GridCells.Fixed(3),
                                    contentPadding = PaddingValues(bottom = 50.dp)
                                ) {
                                    items(state.toppings) {
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
                        Box(
                            modifier = Modifier
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
                                text = "Add to card for ${"$%.2f".format(state.totalPrice)}",
                                onClick = {}
                            )
                        }
                    }
                }
                is ProductDetailsState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize()
                        ,
                        color = AppTheme.colors.primary
                    )
                }
            }

        }
    }
}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        ProductDetailsScreen(
            state = ProductDetailsState.Success(
                totalPrice = 12.99,
                pizza = Pizza(
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
