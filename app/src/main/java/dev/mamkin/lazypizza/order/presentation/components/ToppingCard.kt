package dev.mamkin.lazypizza.order.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import dev.mamkin.lazypizza.order.presentation.productDetails.ToppingUi

@Composable
fun ToppingCard(
    modifier: Modifier = Modifier,
    data: ToppingUi,
    onClick: () -> Unit,
    onPlusClick: () -> Unit,
    onMinusClick: () -> Unit,
) {
    val isAdded = data.count > 0
    val borderColor = if (isAdded) AppTheme.colors.primary else AppTheme.colors.outline

    Card(
        onClick = onClick,
        modifier = modifier.height(142.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surfaceHigher,
            contentColor = AppTheme.colors.textPrimary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    bottom = 12.dp,
                    start = 12.dp,
                    end = 12.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .background(AppTheme.colors.primary8, CircleShape)
            ) {
                AsyncImage(
                    model = data.image,
                    modifier = Modifier.size(56.dp),
                    contentDescription = null,
                    placeholder = BrushPainter(
                        Brush.linearGradient(
                            listOf(
                                Color(color = 0xFFFFFFFF),
                                Color(color = 0xFFDDDDDD),
                            )
                        )
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = data.title,
                style = AppTheme.typography.body3Regular,
                color = AppTheme.colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (isAdded) {
                CountSelector(
                    modifier = Modifier.fillMaxWidth(),
                    count = data.count,
                    onIncrement = onPlusClick,
                    onDecrement = onMinusClick,
                    minusEnabled = data.minusEnabled,
                    plusEnabled = data.plusEnabled
                )
            } else {
                Text(
                    text = "$${data.price}",
                    style = AppTheme.typography.title2,
                    color = AppTheme.colors.textPrimary
                )
            }
        }
    }
}

@Preview
@Composable
private fun ToppingCardPreview() {
    LazyPizzaTheme {
        ToppingCard(
            modifier = Modifier.width(120.dp),
            data = ToppingUi(
                id = "1",
                count = 0,
                title = "Pepperoni",
                price = 10.0,
                image = ""
            ),
            onClick = { },
            onPlusClick = { },
            onMinusClick = { }
        )
    }
}
