package dev.mamkin.lazypizza.home.presentation.productDetails

import dev.mamkin.lazypizza.home.domain.models.Pizza
import dev.mamkin.lazypizza.home.domain.models.Topping

sealed interface ProductDetailsState {
    data object Loading: ProductDetailsState
    data class Success(
        val pizza: Pizza
    ): ProductDetailsState
}