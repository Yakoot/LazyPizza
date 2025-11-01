package dev.mamkin.lazypizza.order.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import dev.mamkin.lazypizza.order.domain.MenuRepository
import dev.mamkin.lazypizza.order.domain.models.Menu
import dev.mamkin.lazypizza.order.domain.models.MenuItem
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseMenuRepository(
    val firestore: FirebaseFirestore,
    val cache: MenuCache
) : MenuRepository {
    override suspend fun getMenu(): Menu = coroutineScope {
        val cachedMenu = cache.getMenu()
        if (cachedMenu != null) {
            return@coroutineScope cachedMenu
        }
        val pizzasDeferred = async {
            firestore.collection("pizza").get().await().documents.map { document ->
                document.toObject(MenuItem.Pizza::class.java)!!.copy(id = document.id)
            }
        }
        val iceCreamsDeferred = async {
            firestore.collection("iceCreams").get().await().documents.map { document ->
                document.toObject(MenuItem.IceCream::class.java)!!.copy(id = document.id)
            }
        }
        val saucesDeferred = async {
            firestore.collection("sauces").get().await().documents.map { document ->
                document.toObject(MenuItem.Sauce::class.java)!!.copy(id = document.id)
            }
        }
        val drinksDeferred = async {
            firestore.collection("drinks").get().await().documents.map { document ->
                document.toObject(MenuItem.Drink::class.java)!!.copy(id = document.id)
            }
        }
        val toppingsDeferred = async {
            firestore.collection("toppings").get().await().documents.map { document ->
                document.toObject(MenuItem.Topping::class.java)!!.copy(id = document.id)
            }
        }

        val menu = Menu(
            pizzas = pizzasDeferred.await(),
            iceCreams = iceCreamsDeferred.await(),
            sauces = saucesDeferred.await(),
            drinks = drinksDeferred.await(),
            toppings = toppingsDeferred.await()
        )
        cache.saveMenu(menu)
        menu
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
