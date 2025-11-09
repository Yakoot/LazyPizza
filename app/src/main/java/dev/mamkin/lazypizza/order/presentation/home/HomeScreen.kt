package dev.mamkin.lazypizza.order.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import coil.compose.AsyncImage
import dev.mamkin.lazypizza.R
import dev.mamkin.lazypizza.core.presentation.designsystem.dialogs.AppConfirmDialog
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import dev.mamkin.lazypizza.order.domain.models.Menu
import dev.mamkin.lazypizza.order.domain.models.MenuItem
import dev.mamkin.lazypizza.order.domain.models.ProductType
import dev.mamkin.lazypizza.order.presentation.components.NavigationChips
import dev.mamkin.lazypizza.order.presentation.components.ProductCard
import dev.mamkin.lazypizza.order.presentation.components.SearchTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
    navigateToDetails: (String) -> Unit,
    navigateToLogIn: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is HomeScreenEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    val onAction = remember {
        { action: HomeAction ->
            when (action) {
                is HomeAction.PizzaClick -> navigateToDetails(action.pizza)
                is HomeAction.LogInClick -> navigateToLogIn()
                else -> viewModel.onAction(action)
            }
        }
    }

    HomeScreen(
        state = state,
        onAction = onAction,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isWideScreen = windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND)
    val columnsCount = if (isWideScreen) 2 else 1

    var isLogOutDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0),
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
                        Spacer(modifier = Modifier.width(12.dp))
                        if (state.isSignedIn) {
                            IconButton(
                                modifier = Modifier.size(32.dp),
                                onClick = {
                                    isLogOutDialogVisible = true
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = AppTheme.colors.primary8,
                                    contentColor = AppTheme.colors.primary
                                ),
                                shape = CircleShape
                            ) {
                                Icon(
                                    modifier = Modifier.size(16.dp),
                                    painter = painterResource(id = R.drawable.log_out),
                                    contentDescription = null
                                )
                            }
                        } else {
                            IconButton(
                                modifier = Modifier.size(32.dp),
                                onClick = {
                                    onAction(HomeAction.LogInClick)
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = AppTheme.colors.textSecondary8,
                                    contentColor = AppTheme.colors.textSecondary
                                ),
                                shape = CircleShape
                            ) {
                                Icon(
                                    modifier = Modifier.size(16.dp),
                                    painter = painterResource(id = R.drawable.user),
                                    contentDescription = null
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            )
        }
    ) { contentPadding ->
        val lazyListState = rememberLazyGridState()
        val coroutineScope = rememberCoroutineScope()

        if (isLogOutDialogVisible) {
            AppConfirmDialog(
                title = "Are you sure you want to log out?",
                confirmButtonText = "Log out",
                dismissButtonText = "Cancel",
                onDismiss = {
                    isLogOutDialogVisible = false
                },
                onConfirm = {
                    isLogOutDialogVisible = false
                    onAction(HomeAction.LogOutClick)
                }
            )
        }

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
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
            NavigationChips(
                onClick = {
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(it)
                    }
                },
                data = state.navigationChips
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (state.noResults) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_results),
                        style = AppTheme.typography.body3Regular,
                        color = AppTheme.colors.textSecondary
                    )
                }
            } else if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = AppTheme.colors.primary
                    )
                }
            } else {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    columns = GridCells.Fixed(columnsCount),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    state.products.forEach { sectionUi ->
                        item(span = { GridItemSpan(columnsCount) }) {
                            Text(
                                text = sectionUi.title,
                                style = AppTheme.typography.label2SemiBold,
                                color = AppTheme.colors.textSecondary
                            )
                        }
                        items(sectionUi.products) {
                            when (it.type) {
                                ProductType.PIZZA -> {
                                    ProductCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        data = it,
                                        imageSize = 108.dp,
                                        imageSectionWidth = 120.dp,
                                        onClick = {
                                            onAction(HomeAction.PizzaClick(it.id))
                                        }
                                    )
                                }

                                else -> {
                                    ProductCard(
                                        data = it,
                                        imageSize = 108.dp,
                                        imageSectionWidth = 120.dp,
                                        onPlusClick = {
                                            onAction(HomeAction.PlusClick(it.id))
                                        },
                                        onMinusClick = {
                                            onAction(HomeAction.MinusClick(it.id))
                                        },
                                        onAddClick = {
                                            onAction(HomeAction.AddClick(it))
                                        },
                                        onDeleteClick = {
                                            onAction(HomeAction.DeleteClick(it.id))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Preview(
    name = "Tablet",
    device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait",
    showSystemUi = true,
)
@Composable
private fun Preview() {
    LazyPizzaTheme {
        HomeScreen(
            state = HomeState(
                isLoading = false,
                products = Menu(
                    pizzas = listOf(
                        MenuItem.Pizza(
                            id = UUID.randomUUID().toString(),
                            title = "Pizza",
                            ingredients = "ingredients",
                            price = 10.0
                        ),
                        MenuItem.Pizza(
                            id = UUID.randomUUID().toString(),
                            title = "Pizza",
                            ingredients = "ingredients",
                            price = 10.0
                        ),
                        MenuItem.Pizza(
                            id = UUID.randomUUID().toString(),
                            title = "Pizza",
                            ingredients = "ingredients",
                            price = 10.0
                        ),
                        MenuItem.Pizza(
                            id = UUID.randomUUID().toString(),
                            title = "Pizza",
                            ingredients = "ingredients",
                            price = 10.0
                        ),
                        MenuItem.Pizza(
                            id = UUID.randomUUID().toString(),
                            title = "Pizza",
                            ingredients = "ingredients",
                            price = 10.0
                        ),
                        MenuItem.Pizza(
                            id = UUID.randomUUID().toString(),
                            title = "Pizza",
                            ingredients = "ingredients",
                            price = 10.0
                        ),
                        MenuItem.Pizza(
                            id = UUID.randomUUID().toString(),
                            title = "Pizza",
                            ingredients = "ingredients",
                            price = 10.0
                        ),
                        MenuItem.Pizza(
                            id = UUID.randomUUID().toString(),
                            title = "Pizza",
                            ingredients = "ingredients",
                            price = 10.0
                        ),
                        MenuItem.Pizza(
                            id = UUID.randomUUID().toString(),
                            title = "Pizza",
                            ingredients = "ingredients",
                            price = 10.0
                        )
                    )
                ).toProductsUi()
            ),
            onAction = {}
        )
    }
}
