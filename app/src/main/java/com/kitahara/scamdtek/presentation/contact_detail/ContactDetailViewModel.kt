package com.kitahara.scamdtek.presentation.contact_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitahara.scamdtek.data.caller_info.Comment
import com.kitahara.scamdtek.data.caller_info.Comment.Companion.toWrapper
import com.kitahara.scamdtek.data.caller_info.RiskDegree
import com.kitahara.scamdtek.domain.ResolveCallerInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ContactDetailViewModel(
    contactNumber: String,
    resolveCallerInfoUseCase: ResolveCallerInfoUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState(contactNumber))
    val viewState get() = _viewState.asStateFlow()

    init {
        resolveCallerInfoUseCase.getCallerInfoWithComments(contactNumber).onEach { riskWithComments ->
            _viewState.update {
                it.copy(
                    riskDegree = riskWithComments?.risk?.riskDegree,
                    comments = riskWithComments?.comments?.toWrapper(),
                    isLoading = riskWithComments == null
                )
            }
        }.launchIn(viewModelScope)
    }

    data class ViewState(
        val contactNumber: String,
        val riskDegree: RiskDegree? = RiskDegree.NOT_DEFINED,
        val comments: List<Comment>? = null,
        val isLoading: Boolean = true
    )
}