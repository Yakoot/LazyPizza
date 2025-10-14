package dev.mamkin.lazypizza.home.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.lazypizza.home.domain.MenuRepository
import dev.mamkin.lazypizza.home.domain.models.Menu
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
    private var initialMenu = Menu()

    private val _state = MutableStateFlow(HomeState())
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
            initialValue = HomeState()
        )

    fun loadMenu() {
        viewModelScope.launch {
            val menu = menuRepository.getMenu()
            initialMenu = menu
            _state.update { it.copy(menu = menu) }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.SearchInput -> onSearchInput(action.value)
            else -> Unit
        }
    }

    private fun onSearchInput(value: String) {
        _state.update {
            it.copy(
                searchValue = value,
                menu = initialMenu.copy(
                    pizzas = initialMenu.pizzas.filter { pizza ->
                        pizza.title.contains(value, ignoreCase = true)
                    },
                    drinks = initialMenu.drinks.filter { drink -> drink.title.contains(value, ignoreCase = true) },
                    sauces = initialMenu.sauces.filter { sauce -> sauce.title.contains(value, ignoreCase = true) },
                    iceCreams = initialMenu.iceCreams.filter { iceCream -> iceCream.title.contains(value, ignoreCase = true) }
                )
            )
        }
    }

}
