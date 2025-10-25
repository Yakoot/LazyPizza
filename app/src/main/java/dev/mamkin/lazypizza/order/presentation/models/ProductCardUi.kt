package dev.mamkin.lazypizza.order.presentation.models

import androidx.compose.runtime.Immutable
import dev.mamkin.lazypizza.order.domain.models.Drink
import dev.mamkin.lazypizza.order.domain.models.IceCream
import dev.mamkin.lazypizza.order.domain.models.Menu
import dev.mamkin.lazypizza.order.domain.models.Pizza
import dev.mamkin.lazypizza.order.domain.models.ProductType
import dev.mamkin.lazypizza.order.domain.models.Sauce
import dev.mamkin.lazypizza.order.domain.models.Topping
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
    val totalPrice: String? = null,
    val priceCalculation: String? = null,
    val priceText: String,
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
    priceText = formatPrice(price),
    image = image,
    showAddButton = false
)

fun Sauce.toProductCardUi() = ProductCardUi(
    id = id,
    type = ProductType.SAUCE,
    title = title,
    price = price,
    priceText = formatPrice(price),
    image = image
)

fun Drink.toProductCardUi() = ProductCardUi(
    id = id,
    type = ProductType.DRINK,
    title = title,
    price = price,
    priceText = formatPrice(price),
    image = image
)

fun IceCream.toProductCardUi() = ProductCardUi(
    id = id,
    type = ProductType.ICECREAM,
    title = title,
    price = price,
    priceText = formatPrice(price),
    image = image
)

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
        toppingItem.toToppingDescription(toppingData ?: Topping())
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
        priceText = formatPrice(totalPrice),
        image = pizza?.image ?: "",
        count = quantity,
        price = totalPrice,
        priceCalculation = getPriceCalculation(pizzaPrice + toppingsTotalPrice, quantity),
        showAddButton = false
    )
}

fun CartItem.Other.toProductCardUi(menu: Menu): ProductCardUi {
    when (productType) {
        ProductType.DRINK -> {
            val drink = menu.drinks.find { it.id == productId }
            val price = drink?.price ?: 0.0
            return ProductCardUi(
                id = id,
                type = ProductType.DRINK,
                title = drink?.title ?: "",
                priceText = formatPrice(price * quantity),
                priceCalculation = getPriceCalculation(price, quantity),
                image = drink?.image ?: "",
                count = quantity,
                price = price,
                showAddButton = false
            )
        }

        ProductType.ICECREAM -> {
            val iceCream = menu.iceCreams.find { it.id == productId }
            val price = iceCream?.price ?: 0.0

            return ProductCardUi(
                id = id,
                type = ProductType.DRINK,
                title = iceCream?.title ?: "",
                priceText = formatPrice(price * quantity),
                priceCalculation = getPriceCalculation(price, quantity),
                image = iceCream?.image ?: "",
                count = quantity,
                price = price,
                showAddButton = false
            )
        }

        ProductType.SAUCE -> {
            val sauce = menu.sauces.find { it.id == productId }
            val price = sauce?.price ?: 0.0

            return ProductCardUi(
                id = id,
                type = ProductType.DRINK,
                title = sauce?.title ?: "",
                priceText = formatPrice(price * quantity),
                priceCalculation = getPriceCalculation(price, quantity),
                image = sauce?.image ?: "",
                count = quantity,
                price = price,
                showAddButton = false
            )
        }

        else -> {
            return ProductCardUi(
                id = id,
                type = ProductType.DRINK,
                title = "",
                priceText = formatPrice(0.0 * quantity),
                priceCalculation = getPriceCalculation(0.0, quantity),
                image = "",
                count = quantity,
                price = 0.0,
                showAddButton = false
            )
        }
    }
}

fun ToppingItem.toToppingDescription(topping: Topping): String {
    return "$quantity x ${topping.title}"
}

//"$%.2f".format(data.price * data.count)
//"${data.count} x $${data.price}"