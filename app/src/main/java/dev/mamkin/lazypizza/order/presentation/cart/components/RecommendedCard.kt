package dev.mamkin.lazypizza.order.presentation.cart.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.mamkin.lazypizza.R
import dev.mamkin.lazypizza.core.presentation.designsystem.buttons.OutlinedIconButton
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import dev.mamkin.lazypizza.order.domain.models.ProductType
import dev.mamkin.lazypizza.order.presentation.cart.RecommendedItemUi

@Composable
fun RecommendedCard(
    modifier: Modifier = Modifier,
    data: RecommendedItemUi,
    onClickAdd: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .width(160.dp)
            .height(202.dp),
        border = BorderStroke(
            width = 1.dp,
            color = AppTheme.colors.surfaceHigher
        ),
        onClick = onClickAdd
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(AppTheme.colors.surfaceHighest),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier.size(108.dp),
                    model = data.image,
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.surfaceHigher)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = data.title,
                    style = AppTheme.typography.body1Regular,
                    color = AppTheme.colors.textSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = data.priceText,
                        style = AppTheme.typography.title1SemiBold,
                        color = AppTheme.colors.textPrimary
                    )
                    OutlinedIconButton(
                        iconColor = AppTheme.colors.primary,
                        iconRes = R.drawable.plus,
                        onClick = onClickAdd
                    )
                }

            }
        }
    }
}

@Preview(widthDp = 400)
@Composable
private fun Preview() {
    var data = remember {
        mutableStateOf(
            RecommendedItemUi(
                title = "Four cheese",
                priceText = "$12.99",
                image = "",
                type = ProductType.PIZZA,
                id = "",
                price = 12.99
            )
        )
    }

    LazyPizzaTheme {
        RecommendedCard(
            data = data.value,
            onClickAdd = {

            },
        )
    }
}



