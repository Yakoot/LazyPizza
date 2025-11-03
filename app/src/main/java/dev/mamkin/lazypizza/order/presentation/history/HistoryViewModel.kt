package dev.mamkin.lazypizza.order.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(HistoryState.NotLoggedIn)
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HistoryState.NotLoggedIn
        )

    fun onAction(action: HistoryAction) {
        when (action) {
            else -> Unit
        }
    }

}