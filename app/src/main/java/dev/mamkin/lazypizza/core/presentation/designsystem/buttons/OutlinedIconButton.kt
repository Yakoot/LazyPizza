package dev.mamkin.lazypizza.core.presentation.designsystem.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.lazypizza.R
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun OutlinedIconButton(
    modifier: Modifier = Modifier,
    iconRes: Int,
    iconColor: Color,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    OutlinedIconButton(
        modifier = modifier.size(22.dp),
        colors = IconButtonDefaults.outlinedIconButtonColors(
            contentColor = iconColor,
            disabledContentColor = AppTheme.colors.outline
        ),
        border = BorderStroke(
            width = 1.dp,
            color = AppTheme.colors.outline50
        ),
        shape = RoundedCornerShape(8.dp),
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(14.dp),
            painter = painterResource(id = iconRes),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        Column {
            OutlinedIconButton(
                iconColor = AppTheme.colors.primary,
                iconRes = R.drawable.trash_04,
                onClick = {},
            )
            OutlinedIconButton(
                iconColor = AppTheme.colors.textSecondary,
                iconRes = R.drawable.plus,
                onClick = {},
            )
            OutlinedIconButton(
                iconColor = AppTheme.colors.textSecondary,
                iconRes = R.drawable.plus,
                enabled = false,
                onClick = {},
            )
        }
    }
}
