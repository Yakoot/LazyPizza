package dev.mamkin.lazypizza.home.presentation.productDetails

import android.R.attr.navigationIcon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import dev.mamkin.lazypizza.R
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import dev.mamkin.lazypizza.home.domain.models.Pizza
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProductDetailsRoot(
    pizza: String,
    navigateBack: () -> Unit,
    viewModel: ProductDetailsViewModel = koinViewModel { parametersOf(pizza) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProductDetailsScreen(
        state = state,
        onAction = {
            when (it) {
                ProductDetailsAction.NavigateBack -> navigateBack()
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
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(topStart = 16.dp),
                            color = AppTheme.colors.surfaceHigher
                        ) {

                        }
                    }
                }
                is ProductDetailsState.Loading -> {
                    CircularProgressIndicator(
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
                pizza = Pizza(
                    id = "1",
                    title = "Four cheese",
                    price = 10.0,
                    image = "",
                    ingredients = "Cheese 1, Cheese 2, Cheese 3, Cheese 4"
                )
            ),
            onAction = {}
        )
    }
}