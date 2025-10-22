package dev.mamkin.lazypizza.order.domain.models

data class Menu(
    val pizzas: List<Pizza> = emptyList(),
    val iceCreams: List<IceCream> = emptyList(),
    val sauces: List<Sauce> = emptyList(),
    val drinks: List<Drink> = emptyList(),
    val toppings: List<Topping> = emptyList()
)