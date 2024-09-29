@file:OptIn(ExperimentalFoundationApi::class)

package com.kitahara.scamdtek.presentation.contact_detail

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kitahara.scamdtek.common.theme.ScamDTekTheme
import com.kitahara.scamdtek.data.contact_number.Comment
import com.kitahara.scamdtek.data.contact_number.Rank
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
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
        enableEdgeToEdge()
        setContent {
            ScamDTekTheme {
                val state by viewModel.viewState.collectAsState()
                LazyColumn {
                    item {
                        val riskyPercentage by remember {
                            derivedStateOf {
                                with(state) {
                                    if (isLoading.not()) {
                                        callerDetails?.riskDegree ?: "Unknown"
                                    } else "Loading..."
                                }
                            }
                        }
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 50.dp),
                            fontSize = 50.sp,
                            textAlign = TextAlign.Center,
                            text = riskyPercentage
                        )
                    }
                    if (state.isLoading) {
                        item { AwaitState("Wait a sec...", true) }
                    } else {
                        state.callerDetails?.comments?.let { comments ->
                            items(comments) { CommentItem(it) }
                        } ?: item { AwaitState("Oops, no result found", false) }
                    }
                }
            }
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
fun CommentItem(comment: Comment) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(Modifier.padding(horizontal = 4.dp, vertical = 2.dp)) {
            ChipItem(text = comment.rank.text, color = comment.rank.color)
            Spacer(Modifier.height(8.dp))
            Text(text = comment.text)
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                thickness = 2.dp
            )
        }

        val formatter = DateTimeFormat.forPattern("dd.MM.yy")
        val parsedTime = formatter.print(comment.addedAt)
        ChipItem(modifier = Modifier.align(Alignment.TopEnd), parsedTime)
    }
}

@Composable
fun ChipItem(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.White
) {
    AssistChip(
        modifier = modifier,
        onClick = {},
        colors = AssistChipDefaults.assistChipColors(containerColor = color),
        label = {
            Text(
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                textAlign = TextAlign.Center,
                text = text
            )
        }
    )
}

@Preview
@Composable
fun CommentItemPreview() {
    CommentItem(Comment(rank = Rank.ANNOYING, text = "+380888284402", addedAt = DateTime()))
}