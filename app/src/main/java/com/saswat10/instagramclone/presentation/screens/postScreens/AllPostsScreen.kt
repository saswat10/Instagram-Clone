package com.saswat10.instagramclone.presentation.screens.postScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import com.saswat10.instagramclone.R
import com.saswat10.instagramclone.navigation.MainNavRoutes
import com.saswat10.instagramclone.presentation.components.common.Avatar
import com.saswat10.instagramclone.presentation.components.posts.Comment
import com.saswat10.instagramclone.presentation.components.posts.PostCard
import com.saswat10.instagramclone.presentation.components.posts.mediaList
import com.saswat10.instagramclone.presentation.components.posts.mediaList2
import com.saswat10.instagramclone.presentation.components.posts.mediaList3
import com.saswat10.instagramclone.presentation.components.user.ImageSizes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllPostsScreen(navigateTo: ((id: Any) -> Unit)) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }

        LazyColumn {

            item {
                PostCard(onComment = { showBottomSheet = true }, mediaList)
                PostCard(onComment = { showBottomSheet = true }, mediaList2)
                PostCard(onComment = { showBottomSheet = true }, mediaList3)
                Spacer(modifier = Modifier.height(110.dp))
            }
        }

        var value by remember { mutableStateOf("") }
        FloatingActionButton(
            onClick = {
                navigateTo(MainNavRoutes.CreatePostScreen)
            }, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, "Add")
        }
        if (showBottomSheet) {
            ModalBottomSheet(

                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Column() {
                    // Sheet content
                    LazyColumn {
                        stickyHeader {
                            Row(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Comments")
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    modifier = Modifier.clickable { showBottomSheet = false })
                            }
                            HorizontalDivider()
                            Row(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                OutlinedTextField(
                                    placeholder = { Text("Add Your Comment") },
                                    value = "",
                                    onValueChange = {},
                                    shape = MaterialTheme.shapes.extraLarge,
                                    modifier = Modifier.weight(1f),
                                    trailingIcon = {
                                        FilledTonalIconButton(
                                            onClick = {}, modifier = Modifier.padding(4.dp)
                                        ) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.Send,
                                                "",
                                            )
                                        }
                                    }
                                )

                            }
                            HorizontalDivider()
                        }
                        items(15) {
                            Column(Modifier.padding(horizontal = 12.dp)) {
                                Comment()
                            }
                        }

                    }
                }
            }
        }
    }
}