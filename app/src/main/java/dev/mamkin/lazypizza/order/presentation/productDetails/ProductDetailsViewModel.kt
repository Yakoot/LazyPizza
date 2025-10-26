package dev.mamkin.lazypizza.order.presentation.productDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.lazypizza.order.domain.CartRepository
import dev.mamkin.lazypizza.order.domain.MenuRepository
import dev.mamkin.lazypizza.order.domain.models.MenuItem
import dev.mamkin.lazypizza.order.domain.models.cart.CartItem
import dev.mamkin.lazypizza.order.domain.models.cart.ToppingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    val pizza: String,
    val menuRepository: MenuRepository,
    val cartRepository: CartRepository
) : ViewModel() {

    private var hasLoadedInitialData = false
    private var selectedToppings: Map<String, Int> = emptyMap()
    private var allToppings: List<MenuItem.Topping> = emptyList()
    private var basePizzaPrice: Double = 0.0


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
            allToppings = menu.toppings
            basePizzaPrice = pizzaData?.price ?: 0.0
            _state.update {
                ProductDetailsState.Success(
                    pizza = pizzaData ?: MenuItem.Pizza(),
                    toppings = menu.toppings.map { it.toToppingUi() },
                    totalPrice = pizzaData?.price ?: 0.0
                )
            }
        }
    }

    private fun updateState(pizzaData: MenuItem.Pizza? = null) {
        _state.update { currentState ->
            val pizza =
                pizzaData ?: (currentState as? ProductDetailsState.Success)?.pizza
                ?: MenuItem.Pizza()

            val toppingsUi = allToppings.map { topping ->
                val count = selectedToppings[topping.id] ?: 0
                topping.toToppingUi().copy(
                    count = count,
                    plusEnabled = count < 3
                )
            }

            val totalPrice = calculateTotalPrice()

            ProductDetailsState.Success(
                pizza = pizza,
                toppings = toppingsUi,
                totalPrice = totalPrice
            )
        }
    }

    private fun calculateTotalPrice(): Double {
        var total = basePizzaPrice

        selectedToppings.forEach { (toppingId, count) ->
            val topping = allToppings.find { it.id == toppingId }
            topping?.let {
                total += it.price * count
            }
        }

        return total
    }

    fun onAction(action: ProductDetailsAction) {
        when (action) {
            is ProductDetailsAction.AddTopping -> addTopping(action.id)
            is ProductDetailsAction.RemoveTopping -> removeTopping(action.id)
            is ProductDetailsAction.ToppingClick -> onToppingClick(action.id)
            ProductDetailsAction.AddToCart -> addToCart()
            else -> Unit
        }
    }


    private fun onToppingClick(id: String) {
        addTopping(id)
    }

    private fun removeTopping(id: String) {
        val currentCount = selectedToppings[id] ?: 0
        selectedToppings = if (currentCount > 1) {
            selectedToppings + (id to currentCount - 1)
        } else {
            selectedToppings - id
        }

        updateState()
    }

    private fun addTopping(id: String) {
        val currentCount = selectedToppings[id] ?: 0
        if (currentCount < 3) {
            selectedToppings = selectedToppings + (id to currentCount + 1)
            updateState()
        }
    }

    private fun addToCart() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is ProductDetailsState.Success) {
                cartRepository.addItem(
                    CartItem.Pizza(
                        productId = pizza,
                        quantity = 1,
                        toppings = selectedToppings.map {
                            ToppingItem(
                                productId = it.key,
                                quantity = it.value
                            )
                        }
                    )
                )
            }
        }
    }

}
