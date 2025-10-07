package dev.mamkin.lazypizza.app.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dev.mamkin.lazypizza.app.LazyPizzaApp
import dev.mamkin.lazypizza.home.data.FirebaseMenuRepository
import dev.mamkin.lazypizza.home.domain.MenuRepository
import dev.mamkin.lazypizza.home.domain.models.Pizza
import dev.mamkin.lazypizza.home.presentation.home.HomeViewModel
import dev.mamkin.lazypizza.home.presentation.productDetails.ProductDetailsViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
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

    factory<MenuRepository> {
        FirebaseMenuRepository(
            firestore = get()
        )
    }

    viewModelOf(::HomeViewModel)

    viewModel { parameters -> ProductDetailsViewModel(pizza = parameters.get(), menuRepository = get()) }
}
