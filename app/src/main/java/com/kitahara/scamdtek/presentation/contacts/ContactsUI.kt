package com.kitahara.scamdtek.presentation.contacts

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kitahara.scamdtek.common.ui.ChipPreference
import com.kitahara.scamdtek.common.ui.InformationWithSideChipsItem

@Composable
fun ContactsUI(
    modifier: Modifier = Modifier,
    contacts: List<ContactsViewModel.CallerInfoItem>
) {
    // TODO add collapseTopAppBAr
    LazyColumn(modifier) {
        items(contacts) { contact ->
            InformationWithSideChipsItem(
                contact.number,
                rightChipPreference = ChipPreference(
                    contact.riskDegree.text,
                    contact.riskDegree.color
                ),
                leftChipPreference = null
            )
        }
    }
}