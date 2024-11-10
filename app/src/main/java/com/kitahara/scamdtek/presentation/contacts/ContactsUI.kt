package com.kitahara.scamdtek.presentation.contacts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import com.kitahara.scamdtek.common.ui.ChipPreference
import com.kitahara.scamdtek.common.ui.InformationWithSideChipsItem
import com.kitahara.scamdtek.presentation.contact_detail.ContactDetailActivity.Companion.launchContactDetailActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsUI(
    modifier: Modifier = Modifier,
    contacts: List<ContactsViewModel.CallerInfoItem>
) {
    val context = LocalContext.current
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            MediumTopAppBar(title = {
                Text(text = "Possible scammers")
            }, scrollBehavior = scrollBehavior)
        }
    ) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            items(contacts + contacts + contacts + contacts + contacts + contacts + contacts) { contact ->
                InformationWithSideChipsItem(
                    modifier = Modifier.clickable {
                        context.launchContactDetailActivity(contact.number)
                    },
                    contact.number,
                    leftChipPreference = ChipPreference(
                        contact.riskDegree.text,
                        contact.riskDegree.color
                    ),
                    rightChipPreference = null
                )
            }
        }
    }
}