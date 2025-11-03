package dev.mamkin.lazypizza.order.presentation.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mamkin.lazypizza.R
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import dev.mamkin.lazypizza.order.presentation.history.components.NotSignedIn
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
