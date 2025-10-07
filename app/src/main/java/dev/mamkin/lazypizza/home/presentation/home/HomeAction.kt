package dev.mamkin.lazypizza.home.presentation.home

import dev.mamkin.lazypizza.home.domain.models.Pizza

sealed interface HomeAction {
    data class PizzaClick(val pizza: String): HomeAction
}