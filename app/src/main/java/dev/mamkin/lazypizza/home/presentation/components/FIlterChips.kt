package dev.mamkin.lazypizza.home.presentation.components

import android.R.attr.enabled
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun FilterChips(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            onClick = { /*TODO*/ },
            label = "Pizza",
        )
        FilterChip(
            onClick = { /*TODO*/ },
            label = "Drinks",
        )
        FilterChip(
            onClick = { /*TODO*/ },
            label = "Sauces",
        )
        FilterChip(
            onClick = { /*TODO*/ },
            label = "Ice Cream",
        )
    }
}

@Composable
private fun FilterChip(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    AssistChip(
        modifier = modifier,
        onClick = onClick,
        label = {
            Text(
                text = label,
                style = AppTheme.typography.body3Medium,
                color = AppTheme.colors.textPrimary
            )
        },
        shape = RoundedCornerShape(8.dp),
        border = AssistChipDefaults.assistChipBorder(
            enabled = true,
            borderColor = AppTheme.colors.outline,
            borderWidth = 1.dp
        )
    )
}

@Preview
@Composable
private fun FilterChipsPreview() {
    LazyPizzaTheme {
        FilterChips()
    }
}