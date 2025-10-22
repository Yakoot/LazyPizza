package dev.mamkin.lazypizza.order.presentation.home

import androidx.compose.runtime.Immutable
import dev.mamkin.lazypizza.order.domain.models.Menu
import dev.mamkin.lazypizza.order.domain.models.ProductType
import dev.mamkin.lazypizza.order.presentation.models.ProductCardUi
import dev.mamkin.lazypizza.order.presentation.models.toProductCardUi

@Immutable
data class HomeState(
    val navigationChips: List<NavigationChipData> = emptyList(),
    val products: List<ProductSectionUi> = emptyList(),
    val searchValue: String = "",
    val noResults: Boolean = false,
    val isLoading: Boolean = false,
)

@Immutable
data class NavigationChipData(
    val title: String,
    val indexToScroll: Int
)

@Immutable
data class ProductSectionUi(
    val title: String,
    val products: List<ProductCardUi>,
)


fun Menu.toProductsUi(): List<ProductSectionUi> {
    return listOf(
        ProductSectionUi(
            title = "PIZZA",
            products = pizzas.map { it.toProductCardUi() }
        ),
        ProductSectionUi(
            title = "DRINKS",
            products = drinks.map { it.toProductCardUi() }
        ),
        ProductSectionUi(
            title = "SAUCES",
            products = sauces.map { it.toProductCardUi() }
        ),
        ProductSectionUi(
            title = "ICE CREAM",
            products = iceCreams.map { it.toProductCardUi() }
        )
    ).filter { it.products.isNotEmpty() }
}

fun List<ProductSectionUi>.toNavigationChips(): List<NavigationChipData> {
    var currentIndex = 0
    return map { productSectionUi ->
        val title = when (productSectionUi.products.first().type) {
            ProductType.PIZZA -> "Pizza"
            ProductType.DRINK -> "Drinks"
            ProductType.SAUCE -> "Sauces"
            ProductType.ICECREAM -> "Ice Cream"
            else -> ""
        }
        val indexToScroll = currentIndex
        currentIndex += 1
        currentIndex += productSectionUi.products.size
        NavigationChipData(
            title = title,
            indexToScroll = indexToScroll
        )
    }
}


