package dev.mamkin.lazypizza.home.domain

import dev.mamkin.lazypizza.home.domain.models.Menu

interface MenuRepository {
    suspend fun getMenu(): Menu
}