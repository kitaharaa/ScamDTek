package com.kitahara.scamdtek.presentation.contact_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitahara.scamdtek.data.contact_number.PhoneNumberRiskDetails
import com.kitahara.scamdtek.domain.GetCallerInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactDetailViewModel(
    private val contactNumber: String,
    private val getCallerDetailsUseCase: GetCallerInfoUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState get() = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            val details = getCallerDetailsUseCase(contactNumber)

            _viewState.update {
                it.copy(
                    callerDetails = details,
                    isLoading = false
                )
            }
        }
    }

    data class ViewState(
        val callerDetails: PhoneNumberRiskDetails? = null,
        val isLoading: Boolean = true
    )
}