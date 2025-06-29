package com.saswat10.instagramclone.screens.searchScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saswat10.instagramclone.components.user.ProfileCard
import com.saswat10.instagramclone.viewmodels.SearchScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel()
) {

    val state by viewModel.viewState.collectAsState()
    var searchText by rememberSaveable { mutableStateOf("") }

    val gridState = rememberLazyGridState()

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            item(span = { GridItemSpan(3) }) {
                Column {
                    Text(
                        "Discover People",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(vertical = 10.dp, horizontal = 4.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            cursorBrush = Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary
                                )
                            ),
                            textStyle = TextStyle.Default.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clip(MaterialTheme.shapes.extraSmall)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(8.dp)
                        )
                        AnimatedVisibility(visible = (searchText.isNotEmpty())) {
                            IconButton(onClick = { searchText = "" }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Search")
                            }
                        }

                    }
                }

            }
            items(state.users.size) {
                val user = state.users[it]
                if (user != null) {
                    ProfileCard(user, onClickFollow = {
                        viewModel.followUser(user.userId)
                    })
                }
            }
            item(span = { GridItemSpan(3) }) {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

    }
}