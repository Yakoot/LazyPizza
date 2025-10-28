package dev.mamkin.lazypizza.order.presentation.productDetails

sealed interface ProductDetailsScreenEvent {
    object NavigateBack : ProductDetailsScreenEvent
}
