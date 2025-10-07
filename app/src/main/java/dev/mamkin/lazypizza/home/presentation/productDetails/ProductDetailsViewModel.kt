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
                    pizza = pizzaData ?: Pizza()
                )
            }
        }
    }

    fun onAction(action: ProductDetailsAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}