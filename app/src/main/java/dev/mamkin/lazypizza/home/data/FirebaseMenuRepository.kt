package dev.mamkin.lazypizza.home.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import dev.mamkin.lazypizza.home.domain.MenuRepository
import dev.mamkin.lazypizza.home.domain.models.Drink
import dev.mamkin.lazypizza.home.domain.models.IceCream
import dev.mamkin.lazypizza.home.domain.models.Menu
import dev.mamkin.lazypizza.home.domain.models.Pizza
import dev.mamkin.lazypizza.home.domain.models.Sauce
import dev.mamkin.lazypizza.home.domain.models.Topping
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseMenuRepository(
    val firestore: FirebaseFirestore
) : MenuRepository {
    override suspend fun getMenu(): Menu = coroutineScope {
        val pizzasDeferred = async { firestore.collection("pizza").get().await().toObjects(Pizza::class.java) }
        val iceCreamsDeferred = async { firestore.collection("iceCreams").get().await().toObjects(IceCream::class.java) }
        val saucesDeferred = async { firestore.collection("sauces").get().await().toObjects(Sauce::class.java) }
        val drinksDeferred = async { firestore.collection("drinks").get().await().toObjects(Drink::class.java) }
        val toppingsDeferred = async { firestore.collection("toppings").get().await().toObjects(Topping::class.java) }

        Menu(
            pizzas = pizzasDeferred.await(),
            iceCreams = iceCreamsDeferred.await(),
            sauces = saucesDeferred.await(),
            drinks = drinksDeferred.await(),
            toppings = toppingsDeferred.await()
        )
    }
}

private suspend fun <T> Task<T>.await(): T {
    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener { task ->
            if (task.isSuccessful) {
                cont.resume(task.result)
            } else {
                cont.resumeWithException(task.exception!!)
            }
        }
    }
}
