package dev.mamkin.lazypizza.order.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.mamkin.lazypizza.R
import dev.mamkin.lazypizza.core.presentation.designsystem.buttons.OutlinedIconButton
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun CountSelector(
    modifier: Modifier = Modifier,
    count: Int,
    plusEnabled: Boolean = true,
    minusEnabled: Boolean = true,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedIconButton(
            iconRes = R.drawable.minus,
            iconColor = AppTheme.colors.textSecondary,
            onClick = onDecrement,
            enabled = minusEnabled
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            text = count.toString(),
            style = AppTheme.typography.title2,
            color = AppTheme.colors.textPrimary,
        )
        OutlinedIconButton(
            iconRes = R.drawable.plus,
            iconColor = AppTheme.colors.textSecondary,
            onClick = onIncrement,
            enabled = plusEnabled
        )
    }
}

@Preview(widthDp = 100)
@Composable
private fun Preview() {
    LazyPizzaTheme {
        CountSelector(
            count = 1,
            onIncrement = {},
            onDecrement = {},
        )
    }

}
