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
        val pizzasDeferred = async {
            firestore.collection("pizza").get().await().documents.map { document ->
                document.toObject(Pizza::class.java)!!.copy(id = document.id)
            }
        }
        val iceCreamsDeferred = async {
            firestore.collection("iceCreams").get().await().documents.map { document ->
                document.toObject(IceCream::class.java)!!.copy(id = document.id)
            }
        }
        val saucesDeferred = async {
            firestore.collection("sauces").get().await().documents.map { document ->
                document.toObject(Sauce::class.java)!!.copy(id = document.id)
            }
        }
        val drinksDeferred = async {
            firestore.collection("drinks").get().await().documents.map { document ->
                document.toObject(Drink::class.java)!!.copy(id = document.id)
            }
        }
        val toppingsDeferred = async {
            firestore.collection("toppings").get().await().documents.map { document ->
                document.toObject(Topping::class.java)!!.copy(id = document.id)
            }
        }

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
