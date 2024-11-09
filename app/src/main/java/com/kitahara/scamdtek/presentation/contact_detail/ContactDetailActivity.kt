package com.kitahara.scamdtek.presentation.contact_detail

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kitahara.scamdtek.common.theme.ScamDTekTheme
import com.kitahara.scamdtek.common.ui.ChipPreference
import com.kitahara.scamdtek.common.ui.InformationWithSideChipsItem
import org.koin.androidx.scope.ScopeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ContactDetailActivity : ScopeActivity() {

    private val contactNumber by lazy {
        intent?.extras?.getString(EXTRA_CONTACT_NUMBER) ?: throw Exception("Incorrect phone number")
    }

    private val viewModel by viewModel<ContactDetailViewModel>(
        parameters = { parametersOf(contactNumber) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setShowWhenLocked(true)
        enableEdgeToEdge()
        setContent {
            val state by viewModel.viewState.collectAsState()
            ContactDetails(state)
        }
    }

    companion object {
        const val EXTRA_CONTACT_NUMBER = "ContactNumber"

        fun Context.launchContactDetailActivity(contactNumber: String) {
            Intent(this, ContactDetailActivity::class.java).apply {
                putExtra(EXTRA_CONTACT_NUMBER, contactNumber)
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_SINGLE_TOP
                startActivity(this)
            }
        }

    }
}


@Composable
fun ContactDetails(state: ContactDetailViewModel.ViewState) {
    ScamDTekTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                item {
                    val riskyPercentage = if (state.isLoading) {
                        "Loading"
                    } else state.riskDegree?.text ?: "0%"
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp),
                        fontSize = 50.sp,
                        textAlign = TextAlign.Center,
                        text = riskyPercentage
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 30.dp),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        text = state.contactNumber
                    )
                }
                if (state.isLoading) {
                    item { AwaitState("Wait a sec...", true) }
                } else {
                    state.comments?.let { comments ->
                        items(comments) {
                            InformationWithSideChipsItem(
                                baseText = it.text,
                                leftChipPreference = ChipPreference(
                                    it.riskDegree.text,
                                    it.riskDegree.color
                                ),
                                rightChipPreference = ChipPreference(it.formatDate)
                            )
                        }
                    } ?: item { AwaitState("Oops, no result found", false) }
                }
            }
        }
    }
}

@Composable
fun AwaitState(text: String, showProgressBar: Boolean) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showProgressBar)
            CircularProgressIndicator()
        Spacer(Modifier.height(10.dp))
        Text(text = text)
    }
}

@Composable
@Preview
fun ContactDetailsPreview() {
    ContactDetails(ContactDetailViewModel.ViewState(contactNumber = "+3809565701"))
}