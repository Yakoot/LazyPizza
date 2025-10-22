package dev.mamkin.lazypizza.order.presentation.productDetails

import dev.mamkin.lazypizza.order.domain.models.Pizza
import dev.mamkin.lazypizza.order.domain.models.Topping

sealed interface ProductDetailsState {
    data object Loading : ProductDetailsState
    data class Success(
        val pizza: Pizza,
        val toppings: List<ToppingUi> = emptyList(),
        val totalPrice: Double = 0.0
    ) : ProductDetailsState
}

data class ToppingUi(
    val id: String,
    val title: String,
    val price: Double,
    val count: Int,
    val image: String,
    val plusEnabled: Boolean = true,
    val minusEnabled: Boolean = true
)

fun Topping.toToppingUi() = ToppingUi(
    id = id,
    title = title,
    price = price,
    image = image,
    count = 0
)
