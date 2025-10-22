package dev.mamkin.lazypizza.order.presentation.history.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.lazypizza.R
import dev.mamkin.lazypizza.core.presentation.designsystem.buttons.FilledButton
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun NotSignedIn(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.history_not_signed_in_title),
            style = AppTheme.typography.title1Medium,
            color = AppTheme.colors.textPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.history_not_signed_in_description),
            style = AppTheme.typography.body3Regular,
            color = AppTheme.colors.textSecondary
        )
        Spacer(modifier = Modifier.height(20.dp))
        FilledButton(
            modifier = Modifier,
            text = stringResource(R.string.history_not_signed_in_button),
            onClick = onClick,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        NotSignedIn(onClick = {})
    }
}