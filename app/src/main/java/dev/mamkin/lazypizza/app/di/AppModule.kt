package dev.mamkin.lazypizza.app.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dev.mamkin.lazypizza.app.LazyPizzaApp
import dev.mamkin.lazypizza.auth.data.FirebaseAuthRepository
import dev.mamkin.lazypizza.auth.domain.AuthRepository
import dev.mamkin.lazypizza.auth.presentation.signin.SignInViewModel
import dev.mamkin.lazypizza.auth.presentation.signin.components.otp.OtpViewModel
import dev.mamkin.lazypizza.order.data.FirebaseMenuRepository
import dev.mamkin.lazypizza.order.data.LocalCartRepository
import dev.mamkin.lazypizza.order.data.MenuCache
import dev.mamkin.lazypizza.order.domain.CartRepository
import dev.mamkin.lazypizza.order.domain.MenuRepository
import dev.mamkin.lazypizza.order.presentation.cart.CartViewModel
import dev.mamkin.lazypizza.order.presentation.history.HistoryViewModel
import dev.mamkin.lazypizza.order.presentation.home.HomeViewModel
import dev.mamkin.lazypizza.order.presentation.productDetails.ProductDetailsViewModel
import kotlinx.coroutines.CoroutineScope
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

    single<FirebaseAuth> {
        Firebase.auth
    }

    single<AuthRepository> {
        FirebaseAuthRepository(
            auth = get()
        )
    }

    single<CartRepository> {
        LocalCartRepository(
            context = androidContext(),
        )
    }

    single { MenuCache() }

    factory<MenuRepository> {
        FirebaseMenuRepository(
            firestore = get(),
            cache = get()
        )
    }

    viewModelOf(::HomeViewModel)
    viewModelOf(::CartViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::OtpViewModel)
    viewModelOf(::SignInViewModel)

    viewModel { parameters ->
        ProductDetailsViewModel(
            pizza = parameters.get(),
            menuRepository = get(),
            cartRepository = get()
        )
    }
}
