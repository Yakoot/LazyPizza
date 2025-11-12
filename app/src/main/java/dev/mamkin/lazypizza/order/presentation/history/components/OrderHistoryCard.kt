package dev.mamkin.lazypizza.order.presentation.history.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun OrderHistoryCard(
    modifier: Modifier = Modifier,
    data: OrderHistoryCardUi
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        border = BorderStroke(
            width = 1.dp,
            color = AppTheme.colors.surfaceHigher
        ),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surfaceHigher
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Text(
                    text = "Order #${data.id}",
                    style = AppTheme.typography.title3,
                    color = AppTheme.colors.textPrimary
                )
                Text(
                    text = data.date,
                    style = AppTheme.typography.body4Regular,
                    color = Color(0xFF627686)
                )
                Spacer(modifier = Modifier.height(16.dp))
                data.items.map {
                    Text(
                        text = it,
                        style = AppTheme.typography.body4Regular,
                        color = Color(0xFF101C28)
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                StatusLabel(
                    type = data.status
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Total amount:",
                    style = AppTheme.typography.body4Regular,
                    color = AppTheme.colors.textSecondary
                )
                Text(
                    text = data.totalAmount,
                    style = AppTheme.typography.title3,
                    color = AppTheme.colors.textPrimary
                )
            }
        }
    }
}

data class OrderHistoryCardUi(
    val id: String,
    val status: StatusLabelType,
    val date: String,
    val items: List<String>,
    val totalAmount: String
)

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OrderHistoryCard(
                data = OrderHistoryCardUi(
                    id = "12347",
                    date = "September 25, 12:15",
                    totalAmount = "\$8.99",
                    status = StatusLabelType.IN_PROGRESS,
                    items = listOf("1 x Margherita")
                )
            )
            OrderHistoryCard(
                data = OrderHistoryCardUi(
                    id = "12347",
                    date = "September 25, 12:15",
                    totalAmount = "\$25.45",
                    status = StatusLabelType.COMPLETED,
                    items = listOf("1 x Margherita", "2 x Pepsi", "2 x Cookies Ice Cream")
                )
            )
            OrderHistoryCard(
                data = OrderHistoryCardUi(
                    id = "12347",
                    date = "September 25, 12:15",
                    totalAmount = "\$11.78",
                    status = StatusLabelType.COMPLETED,
                    items = listOf("1 x Margherita", "2 x Cookies Ice Cream")
                )
            )
        }

    }
}