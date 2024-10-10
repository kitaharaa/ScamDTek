package com.kitahara.scamdtek.presentation.contact_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitahara.scamdtek.data.contact_number.Comment
import com.kitahara.scamdtek.data.contact_number.Comment.Companion.toWrapper
import com.kitahara.scamdtek.data.database.dao.RiskWithCommentsDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ContactDetailViewModel(
    contactNumber: String,
    dao: RiskWithCommentsDao // TODO move to useCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState(contactNumber))
    val viewState get() = _viewState.asStateFlow()

    init {
        dao.getRiskWithComments(contactNumber).onEach { riskWithComments ->
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
        val riskDegree: String? = null,
        val comments: List<Comment>? = null,
        val isLoading: Boolean = true
    )
}