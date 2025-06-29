package com.saswat10.instagramclone.screens.userScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saswat10.instagramclone.components.user.ImageSizes
import com.saswat10.instagramclone.components.user.ProfilePreviewStrip
import com.saswat10.instagramclone.models.domain.User
import com.saswat10.instagramclone.viewmodels.UserViewModel
import com.saswat10.instagramclone.viewmodels.UserViewState
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    userViewModel: UserViewModel = hiltViewModel()
) {
    val userViewState by userViewModel.viewState.collectAsState()


    when (userViewState) {
        is UserViewState.Error -> {}
        is UserViewState.Loading -> {}
        is UserViewState.Success -> {
            Column(modifier = Modifier.fillMaxWidth()) {
                var state by remember { mutableIntStateOf(0) }
                val titles = listOf("Requests", "You")
                Column {
                    val user = (userViewState as UserViewState.Success).user
                    SecondaryTabRow(selectedTabIndex = state) {
                        titles.forEachIndexed { index, title ->
                            Tab(
                                selected = state == index,
                                onClick = { state = index },
                                text = {
                                    Text(
                                        text = title,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                            )
                        }
                    }
                    when (state) {
                        0 -> {
                            RequestTab(user)
                        }

                        1 -> {
                            NotificationTab()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun RequestTab(user: User) {
    val sentRequests = user.sentRequests
    val pendingAccepts = user.pendingRequests
    Timber.d("Sent Requests: ${sentRequests.size}")


    if (sentRequests.isEmpty() && pendingAccepts.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Check Circle",
                modifier = Modifier.size(
                    ImageSizes.LARGE
                )
            )
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = "All Caught Up",
                style = MaterialTheme.typography.titleLarge
            )
        }
    } else {
        LazyColumn {
            items(sentRequests.size) {
                val user = sentRequests[it]
                ProfilePreviewStrip(
                    userPreview = user,
                    text = "Withdraw"
                )
            }
            items(pendingAccepts.size) {
                val user = pendingAccepts[it]
                ProfilePreviewStrip(
                    userPreview = user,
                    text = "Accept"
                )
            }
        }
    }
}

@Composable
fun NotificationTab() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.NotificationsNone,
            contentDescription = "Check Circle",
            modifier = Modifier.size(
                ImageSizes.LARGE
            )
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = "All Caught Up",
            style = MaterialTheme.typography.titleLarge
        )
    }
}