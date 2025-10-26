package dev.mamkin.lazypizza.order.domain.models

import kotlinx.serialization.Serializable

sealed interface MenuItem {
    val id: String
    val title: String
    val price: Double
    val image: String

    @Serializable
    data class Pizza(
        override val id: String = "",
        override val title: String = "",
        override val price: Double = 0.0,
        override val image: String = "",
        val ingredients: String = ""
    ) : MenuItem

    @Serializable
    data class Drink(
        override val id: String = "",
        override val title: String = "",
        override val price: Double = 0.0,
        override val image: String = ""
    ) : MenuItem

    @Serializable
    data class Sauce(
        override val id: String = "",
        override val title: String = "",
        override val price: Double = 0.0,
        override val image: String = ""
    ) : MenuItem

    @Serializable
    data class IceCream(
        override val id: String = "",
        override val title: String = "",
        override val price: Double = 0.0,
        override val image: String = ""
    ) : MenuItem

    @Serializable
    data class Topping(
        override val id: String = "",
        override val title: String = "",
        override val price: Double = 0.0,
        override val image: String = ""
    ) : MenuItem
}

data class Menu(
    val pizzas: List<MenuItem.Pizza> = emptyList(),
    val iceCreams: List<MenuItem.IceCream> = emptyList(),
    val sauces: List<MenuItem.Sauce> = emptyList(),
    val drinks: List<MenuItem.Drink> = emptyList(),
    val toppings: List<MenuItem.Topping> = emptyList()
)

