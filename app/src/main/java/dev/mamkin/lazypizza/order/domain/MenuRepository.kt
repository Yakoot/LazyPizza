package dev.mamkin.lazypizza.order.domain

import dev.mamkin.lazypizza.order.domain.models.Menu

interface MenuRepository {
    suspend fun getMenu(): Menu
}