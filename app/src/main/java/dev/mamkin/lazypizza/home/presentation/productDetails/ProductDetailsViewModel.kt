package dev.mamkin.lazypizza.home.presentation.productDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.lazypizza.home.domain.MenuRepository
import dev.mamkin.lazypizza.home.domain.models.Pizza
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    val pizza: String,
    val menuRepository: MenuRepository,
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow<ProductDetailsState>(ProductDetailsState.Loading)
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
            initialValue = ProductDetailsState.Loading
        )

    private fun loadData() {
        viewModelScope.launch {
            val menu = menuRepository.getMenu()
            val pizzaData = menu.pizzas.find { it.id == pizza }
            _state.update {
                ProductDetailsState.Success(
                    pizza = pizzaData ?: Pizza(),
                    toppings = menu.toppings.map { it.toToppingUi() },
                    totalPrice = pizzaData?.price ?: 0.0
                )
            }
        }
    }

    fun onAction(action: ProductDetailsAction) {
        when (action) {
            is ProductDetailsAction.AddTopping -> addTopping(action.id)
            is ProductDetailsAction.RemoveTopping -> removeTopping(action.id)
            is ProductDetailsAction.ToppingClick -> onToppingClick(action.id)
            else -> Unit
        }
    }

    private fun onToppingClick(id: String) {
        addTopping(id)
    }

    private fun removeTopping(id: String) {
        _state.update {
            if (it is ProductDetailsState.Success) {
                var currentTotal = it.totalPrice
                it.copy(
                    toppings = it.toppings.map {
                        if (it.id == id) {
                            currentTotal -= it.price
                            val newCount = it.count - 1
                            it.copy(count = newCount, plusEnabled = newCount < 3)
                        } else {
                            it
                        }
                    },
                    totalPrice = currentTotal
                )
            } else {
                it
            }
        }
    }

    private fun addTopping(id: String) {
        _state.update { currentState ->
            if (currentState is ProductDetailsState.Success) {
                var currentTotal = currentState.totalPrice

                currentState.copy(
                    toppings = currentState.toppings.map {
                        if (it.id == id) {
                            currentTotal += it.price
                            val newCount = it.count + 1
                            it.copy(count = newCount, plusEnabled = newCount < 3)
                        } else {
                            it
                        }
                    },
                    totalPrice = currentTotal
                )
            } else {
                currentState
            }
        }
    }

}
