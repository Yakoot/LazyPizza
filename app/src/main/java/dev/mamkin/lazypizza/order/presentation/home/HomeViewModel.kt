package dev.mamkin.lazypizza.order.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.lazypizza.order.domain.MenuRepository
import dev.mamkin.lazypizza.order.presentation.utils.getPriceCalculation
import dev.mamkin.lazypizza.order.presentation.utils.getTotalPrice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    val menuRepository: MenuRepository,
) : ViewModel() {
    private var hasLoadedInitialData = false
    private var initialMenuUi = emptyList<ProductSectionUi>()

    private val cart = mutableMapOf<String, Int>()

    private val _state = MutableStateFlow(
        HomeState(
            isLoading = true
        )
    )
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                loadMenu()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeState(
                isLoading = true
            )
        )

    fun loadMenu() {
        viewModelScope.launch {
            val menu = menuRepository.getMenu()
            initialMenuUi = menu.toProductsUi()
            val navigationChips = initialMenuUi.toNavigationChips()
            _state.update {
                it.copy(
                    products = initialMenuUi,
                    navigationChips = navigationChips,
                    isLoading = false
                )
            }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.SearchInput -> onSearchInput(action.value)
            is HomeAction.AddClick -> onAddClick(action.id)
            is HomeAction.DeleteClick -> onDeleteClick(action.id)
            is HomeAction.MinusClick -> onMinusClick(action.id)
            is HomeAction.PlusClick -> onPlusClick(action.id)
            else -> Unit
        }
    }

    private fun onAddClick(id: String) {
        cart[id] = 1
        updateStateWithCart()
    }

    private fun onDeleteClick(id: String) {
        cart.remove(id)
        updateStateWithCart()
    }

    private fun onMinusClick(id: String) {
        val currentQuantity = cart[id] ?: return
        if (currentQuantity > 1) {
            cart[id] = currentQuantity - 1
        } else {
            cart.remove(id)
        }
        updateStateWithCart()
    }

    private fun onPlusClick(id: String) {
        val currentQuantity = cart[id] ?: 0
        cart[id] = currentQuantity + 1
        updateStateWithCart()
    }

    private fun updateStateWithCart(productsToUpdate: List<ProductSectionUi> = _state.value.products) {
        val updatedProducts = productsToUpdate.map { section ->
            section.copy(
                products = section.products.map { product ->
                    val quantityInCart = cart[product.id] ?: 0
                    product.copy(
                        count = quantityInCart,
                        priceCalculation = getPriceCalculation(product.price, quantityInCart),
                        totalPrice = getTotalPrice(product.price, quantityInCart)
                    )
                }
            )
        }

        _state.update {
            it.copy(
                products = updatedProducts
            )
        }
    }

    private fun onSearchInput(value: String) {
        val filteredMenu = if (value.isBlank()) {
            initialMenuUi
        } else {
            initialMenuUi
                .map { section ->
                    section.copy(products = section.products.filter { product ->
                        product.title.contains(value, ignoreCase = true)
                    })
                }
                .filter { section -> section.products.isNotEmpty() }
        }

        val productsWithCartState = filteredMenu.map { section ->
            section.copy(
                products = section.products.map { product ->
                    val quantity = cart[product.id] ?: 0
                    product.copy(
                        count = quantity,
                        priceCalculation = getPriceCalculation(product.price, quantity),
                        totalPrice = getTotalPrice(product.price, quantity)
                    )
                }
            )
        }

        _state.update {
            it.copy(
                searchValue = value,
                products = productsWithCartState,
                navigationChips = productsWithCartState.toNavigationChips(),
                noResults = productsWithCartState.isEmpty()
            )
        }
    }
}
