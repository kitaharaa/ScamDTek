package com.kitahara.scamdtek.presentation.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitahara.scamdtek.data.contact_number.RiskDegree
import com.kitahara.scamdtek.domain.ResolveCallerInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ContactsViewModel(
    resolveCallerInfoUseCase: ResolveCallerInfoUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState get() = _viewState.asStateFlow()

    init {
        resolveCallerInfoUseCase.getAllCallersInfoWithComments().onEach { it ->
            val callerList = it.map { riskWithComments ->
                CallerInfoItem(
                    riskWithComments.risk.phoneNumber,
                    riskWithComments.risk.riskDegree,
                    commentCount = riskWithComments.comments?.size ?: 0
                )
            }
            _viewState.update { state -> state.copy(callerList) }
        }.launchIn(viewModelScope)
    }

    data class CallerInfoItem(
        val number: String,
        val riskDegree: RiskDegree,
        val commentCount: Int
    )

    data class ViewState(val contacts: List<CallerInfoItem> = emptyList())
}