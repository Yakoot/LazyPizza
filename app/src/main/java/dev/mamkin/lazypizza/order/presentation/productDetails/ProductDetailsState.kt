package dev.mamkin.lazypizza.order.presentation.productDetails

import dev.mamkin.lazypizza.order.domain.models.MenuItem

sealed interface ProductDetailsState {
    data object Loading : ProductDetailsState
    data class Success(
        val pizza: MenuItem.Pizza,
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

fun MenuItem.Topping.toToppingUi() = ToppingUi(
    id = id,
    title = title,
    price = price,
    image = image,
    count = 0
)
