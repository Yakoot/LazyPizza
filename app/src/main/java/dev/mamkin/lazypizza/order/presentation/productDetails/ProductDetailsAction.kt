package dev.mamkin.lazypizza.order.presentation.productDetails

sealed interface ProductDetailsAction {
    data object NavigateBack : ProductDetailsAction
    data class ToppingClick(val id: String) : ProductDetailsAction
    data class AddTopping(val id: String) : ProductDetailsAction
    data class RemoveTopping(val id: String) : ProductDetailsAction
    data object AddToCart : ProductDetailsAction
}
