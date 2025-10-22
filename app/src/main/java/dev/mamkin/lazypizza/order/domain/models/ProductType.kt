package dev.mamkin.lazypizza.order.domain.models

import androidx.compose.runtime.Immutable

@Immutable
enum class ProductType {
    PIZZA,
    DRINK,
    SAUCE,
    ICECREAM,
    TOPPING
}
