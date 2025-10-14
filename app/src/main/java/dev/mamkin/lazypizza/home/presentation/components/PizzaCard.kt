package dev.mamkin.lazypizza.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import dev.mamkin.lazypizza.home.domain.models.Pizza

@Composable
fun PizzaCard(
    modifier: Modifier = Modifier,
    data: Pizza,
    onClick: () -> Unit
) {
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
                    .background(AppTheme.colors.surfaceHigher)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = data.title,
                    style = AppTheme.typography.body1Medium,
                    color = AppTheme.colors.textPrimary
                )
                Text(
                    text = data.ingredients,
                    style = AppTheme.typography.body3Regular,
                    color = AppTheme.colors.textSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "$${data.price}",
                    style = AppTheme.typography.title1SemiBold,
                    color = AppTheme.colors.textPrimary
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        PizzaCard(
            data = Pizza(
                title = "Four cheese",
                ingredients = "Cheese 1, Cheese 2, Cheese 3, Cheese 4",
                price = 12.99,
                image = ""
            ),
            onClick = {}
        )
    }
}
