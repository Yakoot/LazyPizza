package dev.mamkin.lazypizza.order.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.lazypizza.auth.domain.AuthRepository
import dev.mamkin.lazypizza.order.presentation.history.components.OrderHistoryCardUi
import dev.mamkin.lazypizza.order.presentation.history.components.StatusLabelType
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
                    _state.value = HistoryState.LoggedIn(
                        items = listOf(
                            OrderHistoryCardUi(
                                id = "12347",
                                date = "September 25, 12:15",
                                totalAmount = "\$8.99",
                                status = StatusLabelType.IN_PROGRESS,
                                items = listOf("1 x Margherita")
                            ),
                            OrderHistoryCardUi(
                                id = "12347",
                                date = "September 25, 12:15",
                                totalAmount = "\$25.45",
                                status = StatusLabelType.COMPLETED,
                                items = listOf(
                                    "1 x Margherita",
                                    "2 x Pepsi",
                                    "2 x Cookies Ice Cream"
                                )
                            ),
                            OrderHistoryCardUi(
                                id = "12347",
                                date = "September 25, 12:15",
                                totalAmount = "\$11.78",
                                status = StatusLabelType.COMPLETED,
                                items = listOf("1 x Margherita", "2 x Cookies Ice Cream")
                            )
                        )
                    )
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
