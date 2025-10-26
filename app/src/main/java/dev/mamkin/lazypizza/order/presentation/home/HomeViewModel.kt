package dev.mamkin.lazypizza.order.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.lazypizza.order.domain.CartRepository
import dev.mamkin.lazypizza.order.domain.MenuRepository
import dev.mamkin.lazypizza.order.domain.models.ProductType
import dev.mamkin.lazypizza.order.domain.models.cart.CartItem
import dev.mamkin.lazypizza.order.presentation.utils.formatPrice
import dev.mamkin.lazypizza.order.presentation.utils.getPriceCalculation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    val menuRepository: MenuRepository,
    val cartRepository: CartRepository
) : ViewModel() {
    private var hasLoadedInitialData = false
    private var initialMenuUi = emptyList<ProductSectionUi>()

    private val _searchQuery = MutableStateFlow("")

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
                observeSearchAndCart()
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

    private fun observeSearchAndCart() {
        viewModelScope.launch {
            combine(
                _searchQuery,
                cartRepository.cart
            ) { query, cart ->
                // Получаем актуальную карту товаров в корзине
                val itemsMap = cart
                    .filterIsInstance<CartItem.Other>()
                    .associate { it.productId to it.quantity }

                // Шаг 1: Фильтрация меню по поисковому запросу
                val filteredMenu = if (query.isBlank()) {
                    initialMenuUi
                } else {
                    initialMenuUi
                        .map { section ->
                            section.copy(products = section.products.filter { product ->
                                product.title.contains(query, ignoreCase = true)
                            })
                        }
                        .filter { it.products.isNotEmpty() }
                }

                // Шаг 2: Обновление отфильтрованного меню данными из корзины
                val newMenuUi = filteredMenu.map { productsSection ->
                    val newProducts = productsSection.products.map { product ->
                        val quantity = itemsMap[product.id] ?: 0
                        product.copy(
                            count = quantity,
                            priceCalculation = getPriceCalculation(product.price, quantity),
                            totalPriceText = formatPrice(product.price * quantity),
                            showAddButton = quantity == 0 && product.type != ProductType.PIZZA
                        )
                    }
                    productsSection.copy(products = newProducts)
                }

                // Шаг 3: Обновление состояния UI
                _state.update {
                    it.copy(
                        isLoading = false,
                        searchValue = query,
                        products = newMenuUi,
                        navigationChips = newMenuUi.toNavigationChips(),
                        noResults = newMenuUi.isEmpty() && query.isNotBlank()
                    )
                }
            }.collect()
        }
    }

    suspend fun loadMenu() {
        val menu = menuRepository.getMenu()
        initialMenuUi = menu.toProductsUi()
    }

    private fun onSearchInput(value: String) {
        _searchQuery.value = value
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.SearchInput -> onSearchInput(action.value)
            is HomeAction.AddClick -> onAddClick(action.id, action.type)
            is HomeAction.DeleteClick -> onDeleteClick(action.id)
            is HomeAction.MinusClick -> onMinusClick(action.id)
            is HomeAction.PlusClick -> onPlusClick(action.id)
            else -> Unit
        }
    }

    private fun onAddClick(id: String, type: ProductType) {
        viewModelScope.launch {
            cartRepository.addItem(
                CartItem.Other(
                    productId = id,
                    quantity = 1,
                    productType = type
                )
            )
        }
    }

    private fun onDeleteClick(id: String) {
        viewModelScope.launch {
            cartRepository.removeItemByProductId(id)
        }
    }

    private fun onMinusClick(id: String) {
        viewModelScope.launch {
            cartRepository.decreaseItemByProductId(id)
        }
    }

    private fun onPlusClick(id: String) {
        viewModelScope.launch {
            cartRepository.increaseItemByProductId(id)
        }
    }
}
