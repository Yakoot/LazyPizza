package dev.mamkin.lazypizza.order.presentation.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import dev.mamkin.lazypizza.R
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import dev.mamkin.lazypizza.order.presentation.history.components.NotSignedIn
import dev.mamkin.lazypizza.order.presentation.history.components.OrderHistoryCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryRoot(
    viewModel: HistoryViewModel = koinViewModel(),
    goToSignIn: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HistoryScreen(
        state = state,
        onAction = {
            when (it) {
                HistoryAction.GoToSignIn -> {
                    goToSignIn()
                }

                else -> Unit
            }
            viewModel.onAction(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    state: HistoryState,
    onAction: (HistoryAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isWideScreen = windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND)
    val columnsCount = if (isWideScreen) 2 else 1

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.history_page_topbar_title),
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
                HistoryState.NotLoggedIn -> {
                    NotSignedIn(
                        modifier = Modifier.padding(top = 120.dp, start = 16.dp, end = 16.dp),
                        onClick = {
                            onAction(HistoryAction.GoToSignIn)
                        }
                    )
                }

                is HistoryState.LoggedIn -> {
                    LazyVerticalStaggeredGrid(
                        contentPadding = PaddingValues(
                            16.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalItemSpacing = 8.dp,
                        columns = StaggeredGridCells.Fixed(columnsCount)
                    ) {
                        items(state.items) {
                            OrderHistoryCard(
                                data = it
                            )
                        }
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
        HistoryScreen(
            state = HistoryState.NotLoggedIn,
            onAction = {}
        )
    }
}
