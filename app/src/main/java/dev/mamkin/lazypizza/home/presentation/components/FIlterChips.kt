package dev.mamkin.lazypizza.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import dev.mamkin.lazypizza.home.presentation.home.NavigationChipData

@Composable
fun NavigationChips(
    modifier: Modifier = Modifier,
    data: List<NavigationChipData>,
    onClick: (Int) -> Unit
) {
    if (data.isNotEmpty()) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            data.forEach {
                FilterChip(
                    onClick = { onClick(it.indexToScroll) },
                    label = it.title
                )
            }
        }
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
private fun NavigationChipsPreview() {
    LazyPizzaTheme {
        NavigationChips(
            onClick = {},
            data = listOf(
                NavigationChipData(
                    title = "Pizza",
                    indexToScroll = 0
                ),
                NavigationChipData(
                    title = "Pizza",
                    indexToScroll = 0
                ),
                NavigationChipData(
                    title = "Pizza",
                    indexToScroll = 0
                ),
                NavigationChipData(
                    title = "Pizza",
                    indexToScroll = 0
                )
            )
        )
    }
}

enum class FilterTarget {
    PIZZA, DRINKS, SAUCES, ICECREAM
}
