package dev.mamkin.lazypizza.home.presentation.productDetails

sealed interface ProductDetailsAction {
    data object NavigateBack: ProductDetailsAction
}