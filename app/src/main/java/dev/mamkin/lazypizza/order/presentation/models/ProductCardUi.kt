package dev.mamkin.lazypizza.order.presentation.models

import androidx.compose.runtime.Immutable
import dev.mamkin.lazypizza.order.domain.models.Menu
import dev.mamkin.lazypizza.order.domain.models.MenuItem
import dev.mamkin.lazypizza.order.domain.models.ProductType
import dev.mamkin.lazypizza.order.domain.models.cart.CartItem
import dev.mamkin.lazypizza.order.domain.models.cart.ToppingItem
import dev.mamkin.lazypizza.order.presentation.utils.formatPrice
import dev.mamkin.lazypizza.order.presentation.utils.getPriceCalculation

@Immutable
data class ProductCardUi(
    val id: String = "",
    val type: ProductType,
    val title: String,
    val description: String? = null,
    val totalPriceText: String = "",
    val priceCalculation: String = "",
    val priceText: String,
    val price: Double,
    val image: String,
    val count: Int = 0,
    val showAddButton: Boolean = true
)

fun MenuItem.toProductCardUi() = when (this) {
    is MenuItem.Pizza -> ProductCardUi(
        id = id,
        type = ProductType.PIZZA,
        title = title,
        description = ingredients,
        price = price,
        priceText = formatPrice(price),
        image = image,
        showAddButton = false
    )

    else -> ProductCardUi(
        id = id,
        type = this.toProductType(),
        title = title,
        price = price,
        priceText = formatPrice(price),
        image = image,
        showAddButton = false
    )
}

fun MenuItem.toProductType() = when (this) {
    is MenuItem.Pizza -> ProductType.PIZZA
    is MenuItem.Drink -> ProductType.DRINK
    is MenuItem.IceCream -> ProductType.ICECREAM
    is MenuItem.Sauce -> ProductType.SAUCE
    is MenuItem.Topping -> ProductType.TOPPING
}


fun CartItem.toProductCardUi(menu: Menu) = when (this) {
    is CartItem.Pizza -> this.toProductCardUi(menu)
    is CartItem.Other -> this.toProductCardUi(menu)
}

fun CartItem.Pizza.toProductCardUi(menu: Menu): ProductCardUi {
    val pizza = menu.pizzas.find { it.id == productId }
    val selectedToppings =
        menu.toppings.filter { toppings.any { toppingItem -> toppingItem.productId == it.id } }
    val toppingDescriptions = toppings.map { toppingItem ->
        val toppingData = selectedToppings.find { it.id == toppingItem.productId }
        toppingItem.toToppingDescription(toppingData ?: MenuItem.Topping())
    }
    val pizzaPrice = (pizza?.price ?: 0.0)
    val toppingsTotalPrice = toppings.sumOf { toppingItem ->
        val toppingData = selectedToppings.find { it.id == toppingItem.productId }
        (toppingData?.price ?: 0.0) * toppingItem.quantity
    }
    val totalPrice = (pizzaPrice + toppingsTotalPrice) * quantity

    return ProductCardUi(
        id = id,
        type = ProductType.PIZZA,
        title = pizza?.title ?: "",
        description = toppingDescriptions.joinToString("\n"),
        priceText = "",
        totalPriceText = formatPrice(totalPrice),
        image = pizza?.image ?: "",
        count = quantity,
        price = pizzaPrice + toppingsTotalPrice,
        priceCalculation = getPriceCalculation(pizzaPrice + toppingsTotalPrice, quantity),
        showAddButton = false
    )
}

fun CartItem.Other.toProductCardUi(menu: Menu): ProductCardUi {
    val product = when (productType) {
        ProductType.DRINK -> menu.drinks.find { it.id == productId }
        ProductType.PIZZA -> menu.pizzas.find { it.id == productId }
        ProductType.SAUCE -> menu.sauces.find { it.id == productId }
        ProductType.ICECREAM -> menu.iceCreams.find { it.id == productId }
        ProductType.TOPPING -> menu.toppings.find { it.id == productId }
    }
    val price = product?.price ?: 0.0

    return ProductCardUi(
        id = id,
        type = ProductType.DRINK,
        title = product?.title ?: "",
        totalPriceText = formatPrice(price * quantity),
        priceText = "",
        priceCalculation = getPriceCalculation(price, quantity),
        image = product?.image ?: "",
        count = quantity,
        price = price,
        showAddButton = false
    )
}

fun ToppingItem.toToppingDescription(topping: MenuItem.Topping): String {
    return "$quantity x ${topping.title}"
}