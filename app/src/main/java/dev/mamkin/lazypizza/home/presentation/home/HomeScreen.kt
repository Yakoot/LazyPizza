package dev.mamkin.lazypizza.home.presentation.home

import android.R.attr.data
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import dev.mamkin.lazypizza.home.presentation.components.FilterChips
import dev.mamkin.lazypizza.home.presentation.components.FilterTarget
import dev.mamkin.lazypizza.home.presentation.components.OtherCard
import dev.mamkin.lazypizza.home.presentation.components.OtherCardData
import dev.mamkin.lazypizza.home.presentation.components.PizzaCard
import dev.mamkin.lazypizza.home.presentation.components.SearchTextField
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
    navigateToDetails: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onAction = {
            when (it) {
                is HomeAction.PizzaClick -> navigateToDetails(it.pizza)
                else -> viewModel.onAction(it)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.statusBarsPadding(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pizza),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "LazyPizza",
                            style = AppTheme.typography.body3Bold,
                            color = AppTheme.colors.textPrimary
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(id = R.drawable.phone_filled),
                            contentDescription = null,
                            tint = AppTheme.colors.textSecondary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+1 (555) 321-7890",
                            style = AppTheme.typography.body1Regular,
                            color = AppTheme.colors.textPrimary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            )
        }
    ) { contentPadding ->
        val lazyListState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                ,
                model = "https://firebasestorage.googleapis.com/v0/b/lazy-pizza-11723.firebasestorage.app/o/banner.webp?alt=media&token=ed92275f-e200-4dc1-9cb8-1f024ba7772d",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = BrushPainter(
                    Brush.linearGradient(
                        listOf(
                            Color(color = 0xFFFFFFFF),
                            Color(color = 0xFFDDDDDD),
                        )
                    )
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            SearchTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.searchValue,
                onValueChange = {
                    onAction(HomeAction.SearchInput(it))
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            FilterChips(
                onClick = {
                    coroutineScope.launch {
                        when (it) {
                            FilterTarget.PIZZA -> {
                                lazyListState.animateScrollToItem(0)
                            }
                            FilterTarget.DRINKS -> {
                                val drinksIndex = state.menu.pizzas.size + 1
                                lazyListState.animateScrollToItem(drinksIndex)
                            }
                            FilterTarget.SAUCES -> {
                                val saucesIndex = state.menu.pizzas.size + state.menu.drinks.size + 2
                                lazyListState.animateScrollToItem(saucesIndex)
                            }
                            FilterTarget.ICECREAM -> {
                                val iceCreamIndex = state.menu.pizzas.size + state.menu.drinks.size + state.menu.sauces.size + 3
                                lazyListState.animateScrollToItem(iceCreamIndex)
                            }
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (state.menu.pizzas.isNotEmpty()) {
                    item {
                        Text(
                            text = "PIZZA",
                            style = AppTheme.typography.label2SemiBold,
                            color = AppTheme.colors.textSecondary
                        )
                    }
                    items(state.menu.pizzas) {
                        PizzaCard(data = it, onClick = {
                            onAction(HomeAction.PizzaClick(it.id))
                        })
                    }

                }

                if (state.menu.drinks.isNotEmpty()) {
                    item {
                        Text(
                            text = "DRINKS",
                            style = AppTheme.typography.label2SemiBold,
                            color = AppTheme.colors.textSecondary
                        )
                    }
                    items(state.menu.drinks) {
                        OtherCard(
                            data = OtherCardData(
                                title = it.title,
                                price = it.price,
                                count = 0,
                                image = it.image
                            ),
                            onIncrement = {},
                            onDecrement = {},
                            onClickAdd = {}
                        )
                    }
                }

                if (state.menu.sauces.isNotEmpty()) {
                    item {
                        Text(
                            text = "SAUCES",
                            style = AppTheme.typography.label2SemiBold,
                            color = AppTheme.colors.textSecondary
                        )
                    }
                    items(state.menu.sauces) {
                        OtherCard(
                            data = OtherCardData(
                                title = it.title,
                                price = it.price,
                                count = 0,
                                image = it.image
                            ),
                            onIncrement = {},
                            onDecrement = {},
                            onClickAdd = {}
                        )
                    }
                }

                if (state.menu.iceCreams.isNotEmpty()) {
                    item {
                        Text(
                            text = "ICE CREAM",
                            style = AppTheme.typography.label2SemiBold,
                            color = AppTheme.colors.textSecondary
                        )
                    }
                    items(state.menu.iceCreams) {
                        OtherCard(
                            data = OtherCardData(
                                title = it.title,
                                price = it.price,
                                count = 0,
                                image = it.image
                            ),
                            onIncrement = {},
                            onDecrement = {},
                            onClickAdd = {}
                        )
                    }
                }
            }

        }
    }
}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        HomeScreen(
            state = HomeState(),
            onAction = {}
        )
    }
}
