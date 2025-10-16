package dev.mamkin.lazypizza.home.presentation.components

import android.R.attr.data
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.mamkin.lazypizza.R
import dev.mamkin.lazypizza.core.presentation.designsystem.buttons.OutlinedButton
import dev.mamkin.lazypizza.core.presentation.designsystem.buttons.OutlinedIconButton
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import dev.mamkin.lazypizza.home.domain.models.ProductType
import dev.mamkin.lazypizza.home.presentation.models.ProductCardUi

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    data: ProductCardUi,
    onClick: () -> Unit = {},
    onClickAdd: () -> Unit = {},
    onIncrement: () -> Unit = {},
    onDecrement: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    val isAdded = data.count > 0
    Card(
        modifier = modifier
            .height(120.dp)
        ,
        border = BorderStroke(
            width = 1.dp,
            color = AppTheme.colors.surfaceHigher
        ),
        onClick = onClick
    ) {
        Row() {
            Box(
                modifier = Modifier
                    .background(AppTheme.colors.surfaceHighest)
                    .width(120.dp)
            ) {
                AsyncImage(
                    modifier = Modifier.padding(6.dp),
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
                Row() {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = data.title,
                        style = AppTheme.typography.body1Medium,
                        color = AppTheme.colors.textPrimary
                    )
                    if (isAdded) {
                        OutlinedIconButton(
                            iconRes = R.drawable.trash_04,
                            iconColor = AppTheme.colors.primary,
                            onClick = onDelete
                        )
                    }

                }

                data.description?.let {
                    Text(
                        text = data.description,
                        style = AppTheme.typography.body3Regular,
                        color = AppTheme.colors.textSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isAdded) {
                        CountSelector(
                            modifier = Modifier.width(96.dp),
                            count = data.count,
                            onIncrement = onIncrement,
                            onDecrement = onDecrement
                        )
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "$%.2f".format(data.price * data.count),
                                style = AppTheme.typography.title1SemiBold,
                                color = AppTheme.colors.textPrimary
                            )
                            Text(
                                text = "${data.count} x $${data.price}",
                                style = AppTheme.typography.body4Regular,
                                color = AppTheme.colors.textSecondary
                            )
                        }
                    } else {
                        Text(
                            text = "$${data.price}",
                            style = AppTheme.typography.title1SemiBold,
                            color = AppTheme.colors.textPrimary
                        )
                        if (data.showAddButton) {
                            OutlinedButton(
                                text = stringResource(R.string.add_to_cart),
                                onClick = onClickAdd
                            )
                        }
                    }
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
            ProductCardUi(
                title = "Four cheese",
                price = 12.99,
                count = 0,
                image = "",
                type = ProductType.PIZZA,
                showAddButton = false
            )
        )
    }

    LazyPizzaTheme {
        ProductCard(
            data = data.value,
            onClickAdd = {
                data.value = data.value.copy(count = 1)
            },
            onIncrement = {
                data.value = data.value.copy(count = data.value.count + 1)
            },
            onDecrement = {
                data.value = data.value.copy(count = data.value.count - 1)
            }
        )
    }
}



