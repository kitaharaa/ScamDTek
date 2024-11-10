package com.kitahara.scamdtek.presentation.contact_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitahara.scamdtek.domain.ResolveCallerInfoItemUseCase
import com.kitahara.scamdtek.domain.model.CallerInfoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ContactDetailViewModel(
    contactNumber: String,
    resolveCallerInfoItemUseCase: ResolveCallerInfoItemUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState(contactNumber))
    val viewState get() = _viewState.asStateFlow()

    init {
        resolveCallerInfoItemUseCase.getCallerInfoWithComments(contactNumber).onEach { callerInfoItem ->
            _viewState.update {
                it.copy(
                    callerInfoItem = callerInfoItem,
                    isLoading = (callerInfoItem == null)
                )
            }
        }.launchIn(viewModelScope)
    }

    data class ViewState(
        val contactNumber: String,
        val callerInfoItem: CallerInfoItem? = null,
        val isLoading: Boolean = true
    )
}