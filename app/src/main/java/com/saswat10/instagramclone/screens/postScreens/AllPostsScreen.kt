package com.saswat10.instagramclone.screens.postScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saswat10.instagramclone.components.posts.PostCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllPostsScreen(updateProfile: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }

        LazyColumn {
            item {
                PostCard(onComment = { showBottomSheet = true })
                PostCard(onComment = { showBottomSheet = true })
                PostCard(onComment = { showBottomSheet = true })
                Spacer(modifier = Modifier.height(200.dp))
            }
        }

        var value by remember { mutableStateOf("") }

        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier
                    .systemBarsPadding()
                    .fillMaxHeight(),
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                // Sheet content
                LazyColumn {

                    stickyHeader {
                        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Post Comments")
                            Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.clickable {showBottomSheet = false})
                        }
                        HorizontalDivider()
                    }

                }


            }
        }

    }
}