package com.kitahara.scamdtek.presentation.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitahara.scamdtek.domain.ResolveCallerInfoItemUseCase
import com.kitahara.scamdtek.domain.model.CallerInfoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ContactsViewModel(
    resolveCallerInfoItemUseCase: ResolveCallerInfoItemUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState get() = _viewState.asStateFlow()

    init {
        resolveCallerInfoItemUseCase.getAllCallersInfoWithComments().onEach { callerInfoItem ->
            _viewState.update { state -> state.copy(contacts = callerInfoItem) }
        }.launchIn(viewModelScope)
    }
    data class ViewState(val contacts: List<CallerInfoItem> = emptyList())
}