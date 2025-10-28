package dev.mamkin.lazypizza.order.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.lazypizza.order.domain.CartRepository
import dev.mamkin.lazypizza.order.domain.MenuRepository
import dev.mamkin.lazypizza.order.domain.models.cart.CartItem
import dev.mamkin.lazypizza.order.presentation.models.toProductCardUi
import dev.mamkin.lazypizza.order.presentation.utils.formatPrice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    private val menuRepository: MenuRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private var allRecommendedItems: List<RecommendedItemUi> = listOf()

    private val _state = MutableStateFlow<CartState>(CartState.Loading)
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                loadData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CartState.Loading
        )

    private fun loadData() {
        viewModelScope.launch {
            val menu = menuRepository.getMenu()
            allRecommendedItems = (menu.drinks + menu.sauces)
                .map { it.toRecommendedItemUi() }
                .shuffled()

            cartRepository.cart.collectLatest { items ->
                val idsPresentedInCart = items.map { it.productId }
                when {
                    items.isEmpty() -> {
                        _state.value = CartState.Empty
                    }

                    else -> {
                        val totalPrice = items.sumOf { item -> item.price * item.quantity }
                        _state.value = CartState.Content(
                            items = items.map { it.toProductCardUi(menu) },
                            recommended = allRecommendedItems.filterNot {
                                idsPresentedInCart.contains(
                                    it.id
                                )
                            },
                            buttonText = "Proceed to Checkout (${formatPrice(totalPrice)})"
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: CartAction) {
        when (action) {
            is CartAction.DeleteClick -> onDeleteClick(action.id)
            is CartAction.MinusClick -> onMinusClick(action.id)
            is CartAction.PlusClick -> onPlusClick(action.id)
            is CartAction.AddRecommendedClick -> onAddRecommendedClick(action.itemUi)
            else -> Unit
        }
    }

    private fun onDeleteClick(id: String) {
        viewModelScope.launch {
            cartRepository.removeItem(id)
        }
    }

    private fun onMinusClick(id: String) {
        viewModelScope.launch {
            cartRepository.decreaseItemById(id)
        }
    }

    private fun onPlusClick(id: String) {
        viewModelScope.launch {
            cartRepository.increaseItemById(id)
        }
    }

    private fun onAddRecommendedClick(itemUi: RecommendedItemUi) {
        viewModelScope.launch {
            cartRepository.addItem(
                CartItem.Other(
                    productId = itemUi.id,
                    quantity = 1,
                    productType = itemUi.type,
                    price = itemUi.price
                )
            )
        }

    }
}
