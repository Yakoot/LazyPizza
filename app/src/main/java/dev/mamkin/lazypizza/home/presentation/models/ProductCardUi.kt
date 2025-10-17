package dev.mamkin.lazypizza.home.presentation.models

import android.R.attr.type
import dev.mamkin.lazypizza.home.domain.models.Drink
import dev.mamkin.lazypizza.home.domain.models.IceCream
import dev.mamkin.lazypizza.home.domain.models.Pizza
import dev.mamkin.lazypizza.home.domain.models.ProductType
import dev.mamkin.lazypizza.home.domain.models.Sauce

data class ProductCardUi(
    val id: String = "",
    val type: ProductType,
    val title: String,
    val description: String? = null,
    val price: Double,
    val image: String,
    val count: Int = 0,
    val showAddButton: Boolean = true
)


fun Pizza.toProductCardUi() = ProductCardUi(
    id = id,
    type = ProductType.PIZZA,
    title = title,
    description = ingredients,
    price = price,
    image = image,
    showAddButton = false
)

fun Sauce.toProductCardUi() = ProductCardUi(
    id = id,
    type = ProductType.SAUCE,
    title = title,
    price = price,
    image = image
)

fun Drink.toProductCardUi() = ProductCardUi(
    id = id,
    type = ProductType.DRINK,
    title = title,
    price = price,
    image = image
)

fun IceCream.toProductCardUi() = ProductCardUi(
    id = id,
    type = ProductType.ICECREAM,
    title = title,
    price = price,
    image = image
)