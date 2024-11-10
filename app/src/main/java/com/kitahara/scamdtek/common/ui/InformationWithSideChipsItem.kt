package com.kitahara.scamdtek.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kitahara.scamdtek.data.caller_info.RiskDegree

data class ChipPreference(
    val text: String,
    private val color: Color? = null,
) {
    val safeColor get() = color ?: Color.White
}

@Composable
fun InformationWithSideChipsItem(
    modifier: Modifier = Modifier,
    baseText: String,
    rightChipPreference: ChipPreference?,
    leftChipPreference: ChipPreference?,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(Modifier.padding(horizontal = 4.dp, vertical = 2.dp)) {
            Box(Modifier.fillMaxWidth()) {
                ChipItem(
                    modifier = Modifier.align(Alignment.TopStart),
                    chipPreference = rightChipPreference
                )
                ChipItem(
                    modifier = Modifier.align(Alignment.TopEnd),
                    chipPreference = leftChipPreference
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(text = baseText)
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                thickness = 2.dp
            )
        }
    }
}

@Composable
fun ChipItem(
    modifier: Modifier = Modifier,
    chipPreference: ChipPreference?
) {
    chipPreference ?: return
    AssistChip(
        modifier = modifier,
        onClick = {},
        colors = AssistChipDefaults.assistChipColors(containerColor = chipPreference.safeColor),
        label = {
            Text(
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                textAlign = TextAlign.Center,
                text = chipPreference.text
            )
        }
    )
}

@Preview
@Composable
fun InformationWithSideChipsItemPreview() {
    InformationWithSideChipsItem(
        baseText = "+380888284402",
        rightChipPreference = ChipPreference(RiskDegree.ANNOYING.text, RiskDegree.ANNOYING.color),
        leftChipPreference = ChipPreference("09.11.24", Color.Magenta)
    )
}