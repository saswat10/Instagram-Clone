package com.saswat10.instagramclone.presentation.screens.userScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saswat10.instagramclone.R
import com.saswat10.instagramclone.presentation.components.common.SimpleHeader
import com.saswat10.instagramclone.presentation.components.user.UserDetailCard
import com.saswat10.instagramclone.navigation.MainNavRoutes
import com.saswat10.instagramclone.viewmodels.UserViewModel
import com.saswat10.instagramclone.viewmodels.UserViewState
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    navigateTo: ((id: Any) -> Unit),
) {
    val state by userViewModel.viewState.collectAsState()
    val refreshState = state is UserViewState.Loading

    val gridState = rememberLazyGridState()
    when (state) {
        is UserViewState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        is UserViewState.Success -> {
            val user = (state as UserViewState.Success).user
            Timber.d("User: $user")

            PullToRefreshBox(
                isRefreshing = refreshState,
                onRefresh = {
                    userViewModel.getUser()
                },

                ) {

                LazyVerticalGrid(
                    state = gridState,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    contentPadding = PaddingValues(1.dp),
                    columns = GridCells.Adaptive(minSize = 120.dp)
                ) {


                    stickyHeader { SimpleHeader("@${user.username}", TextAlign.Center) }
                    item(span = { GridItemSpan(maxLineSpan) }) {

                        Column(modifier = Modifier.padding(16.dp)) {
                            UserDetailCard(user)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {navigateTo(MainNavRoutes.UpdateScreen)},
                                    modifier = Modifier.weight(1f)
                                ) { Text("Edit Profile") }
                                FilledTonalButton(
                                    onClick = {
                                        userViewModel.signOut()
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.elevatedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) { Text("Sign Out") }
                            }


                        }
                    }
                    if (user.posts != 0) {
                        items(15) {
                            Image(
                                painter = painterResource(R.drawable.img1),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clickable(onClick = { Timber.d("") })
                            )
                        }
                    } else {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 120.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp)
                                )
                                Text(
                                    text = "No Posts Yet",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(modifier = Modifier.height(100.dp))
                    }

                }
            }
        }

        is UserViewState.Error -> {
            Text(text = "Error")
        }
    }


}