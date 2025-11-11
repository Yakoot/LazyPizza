package dev.mamkin.lazypizza.order.presentation.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.InstrumentSans
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun StatusLabel(
    modifier: Modifier = Modifier,
    type: StatusLabelType
) {
    val backgroundColor = when (type) {
        StatusLabelType.IN_PROGRESS -> Color(0xFF2E7D32)
        StatusLabelType.COMPLETED -> Color(0xFFF9A825)
    }
    Text(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 2.dp),
        text = type.value,
        color = Color.White,
        style = TextStyle(
            fontFamily = InstrumentSans,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            lineHeight = 16.sp,
        )
    )
}

enum class StatusLabelType(val value: String) {
    IN_PROGRESS("In progress"), COMPLETED("Completed")
}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            StatusLabel(type = StatusLabelType.IN_PROGRESS)
            StatusLabel(type = StatusLabelType.COMPLETED)
        }
    }
}