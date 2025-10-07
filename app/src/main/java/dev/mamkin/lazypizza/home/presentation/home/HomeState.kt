package dev.mamkin.lazypizza.home.presentation.home

import dev.mamkin.lazypizza.home.domain.models.Menu

data class HomeState(
    val menu: Menu = Menu(),
)