package dev.mamkin.lazypizza.order.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.lazypizza.auth.domain.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    val authRepository: AuthRepository,
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow<HistoryState>(HistoryState.NotLoggedIn)
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                subscribeOnAuthState()
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HistoryState.NotLoggedIn
        )

    private fun subscribeOnAuthState() {
        viewModelScope.launch {
            authRepository.observeAuthState().collectLatest {
                if (it) {
                    _state.value = HistoryState.LoggedIn
                } else {
                    _state.value = HistoryState.NotLoggedIn
                }
            }
        }
    }

    fun onAction(action: HistoryAction) {
        when (action) {
            else -> Unit
        }
    }

}