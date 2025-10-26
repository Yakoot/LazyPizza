package dev.mamkin.lazypizza.order.data

import dev.mamkin.lazypizza.order.domain.models.Menu

class MenuCache {
    private var menu: Menu? = null

    fun saveMenu(menu: Menu) {
        this.menu = menu
    }

    fun getMenu(): Menu? {
        return menu
    }
}