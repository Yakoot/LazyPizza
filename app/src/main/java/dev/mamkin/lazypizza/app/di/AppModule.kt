package dev.mamkin.lazypizza.app.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dev.mamkin.lazypizza.app.LazyPizzaApp
import dev.mamkin.lazypizza.order.data.FirebaseMenuRepository
import dev.mamkin.lazypizza.order.data.LocalCartRepository
import dev.mamkin.lazypizza.order.domain.CartRepository
import dev.mamkin.lazypizza.order.domain.MenuRepository
import dev.mamkin.lazypizza.order.presentation.cart.CartViewModel
import dev.mamkin.lazypizza.order.presentation.home.HomeViewModel
import dev.mamkin.lazypizza.order.presentation.productDetails.ProductDetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as LazyPizzaApp).applicationScope
    }

    single<FirebaseFirestore> {
        Firebase.firestore
    }

    single<FirebaseStorage> {
        Firebase.storage
    }

    single {
        Json
    }

    single<CartRepository> {
        LocalCartRepository(
            context = androidContext(),
            json = get()
        )
    }

    factory<MenuRepository> {
        FirebaseMenuRepository(
            firestore = get()
        )
    }

    viewModelOf(::HomeViewModel)
    viewModelOf(::CartViewModel)

    viewModel { parameters ->
        ProductDetailsViewModel(
            pizza = parameters.get(),
            menuRepository = get()
        )
    }
}
