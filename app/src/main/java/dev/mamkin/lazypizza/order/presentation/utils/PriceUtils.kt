package dev.mamkin.lazypizza.order.presentation.utils


fun formatPrice(price: Double): String = "$%.2f".format(price)
fun getTotalPrice(price: Double, count: Int): String = "$%.2f".format(price * count)

fun getPriceCalculation(price: Double, count: Int) = "%d x $%.2f".format(count, price * count)