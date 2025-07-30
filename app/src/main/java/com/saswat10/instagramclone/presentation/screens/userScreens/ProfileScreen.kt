package com.saswat10.instagramclone.presentation.screens.userScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.saswat10.instagramclone.R
import com.saswat10.instagramclone.navigation.MainNavRoutes
import com.saswat10.instagramclone.presentation.components.common.SimpleHeader
import com.saswat10.instagramclone.presentation.components.posts.MediaType
import com.saswat10.instagramclone.presentation.components.user.AvatarDetailCard
import com.saswat10.instagramclone.viewmodels.ProfileViewModel


@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    navigateTo: ((id: Any) -> Unit),
    onSignOut: () -> Unit
) {
    val user by profileViewModel.user.collectAsState()
    val posts by profileViewModel.posts.collectAsState()


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column() {
                SimpleHeader("@" + user.username)
                Column(Modifier.padding(8.dp)) {
                    AvatarDetailCard(user, onSignOut = {
                        profileViewModel.signOut()
                        onSignOut()
                    }, onEdit = { navigateTo(MainNavRoutes.UpdateScreen) })
                }


                HorizontalDivider()
            }
        }
        items(posts) {
            if (it?.media[0]?.type == MediaType.VIDEO) AsyncImage(
                model = R.drawable.astronaut_nord, null, modifier = Modifier.aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            else
                AsyncImage(
                    it?.media[0]?.url,
                    "",
                    modifier = Modifier.aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
        }
    }
}


//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProfileScreen(
//    userViewModel: UserViewModel = hiltViewModel(),
//    navigateTo: ((id: Any) -> Unit),
//    onSignOut: ()->Unit
//) {
//    val state by userViewModel.viewState.collectAsState()
//    val refreshState = state is UserViewState.Loading
//
//    val gridState = rememberLazyGridState()
//    when (state) {
//        is UserViewState.Loading -> {
//            Box(modifier = Modifier.fillMaxSize()) {
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//            }
//        }
//
//        is UserViewState.Success -> {
//            val user = (state as UserViewState.Success).user
//            Timber.d("User: $user")
//
//            PullToRefreshBox(
//                isRefreshing = refreshState,
//                onRefresh = {
//                    userViewModel.getUser()
//                },
//
//                ) {
//
//                LazyVerticalGrid(
//                    state = gridState,
//                    verticalArrangement = Arrangement.spacedBy(2.dp),
//                    horizontalArrangement = Arrangement.spacedBy(2.dp),
//                    contentPadding = PaddingValues(1.dp),
//                    columns = GridCells.Adaptive(minSize = 120.dp)
//                ) {
//
//
//                    stickyHeader { SimpleHeader("@${user.username}", TextAlign.Center) }
//                    item(span = { GridItemSpan(maxLineSpan) }) {
//
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            UserDetailCard(user)
//                            Spacer(modifier = Modifier.height(10.dp))
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.spacedBy(10.dp),
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Icon(Lucide.Search, null)
//                                Button(
//                                    onClick = {navigateTo(MainNavRoutes.UpdateScreen)},
//                                    modifier = Modifier.weight(1f),
//                                ) { Text("Edit Profile") }
//                                FilledTonalButton(
//                                    onClick = {
//                                        userViewModel.signOut()
//                                        onSignOut()
//                                    },
//                                    modifier = Modifier.weight(1f),
//                                    colors = ButtonDefaults.elevatedButtonColors(
//                                        contentColor = MaterialTheme.colorScheme.error
//                                    )
//                                ) { Text("Sign Out") }
//                            }
//
//
//                        }
//                    }
//                    if (user.posts != 0) {
//                        items(15) {
//                            Image(
//                                painter = painterResource(R.drawable.img1),
//                                contentScale = ContentScale.Crop,
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .aspectRatio(1f)
//                                    .clickable(onClick = { Timber.d("") })
//                            )
//                        }
//                    } else {
//                        item(span = { GridItemSpan(maxLineSpan) }) {
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(top = 120.dp),
//                                horizontalAlignment = Alignment.CenterHorizontally
//                            ) {
//                                Icon(
//                                    Icons.Default.CameraAlt,
//                                    contentDescription = null,
//                                    modifier = Modifier.size(40.dp)
//                                )
//                                Text(
//                                    text = "No Posts Yet",
//                                    textAlign = TextAlign.Center,
//                                    style = MaterialTheme.typography.titleLarge
//                                )
//                            }
//                        }
//                    }
//                    item(span = { GridItemSpan(maxLineSpan) }) {
//                        Spacer(modifier = Modifier.height(100.dp))
//                    }
//
//                }
//            }
//        }
//
//        is UserViewState.Error -> {
//            Text(text = "Error")
//        }
//    }
//
//
//}