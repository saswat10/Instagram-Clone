package com.saswat10.instagramclone.screens.userScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.saswat10.instagramclone.R
import com.saswat10.instagramclone.components.common.SimpleHeader
import com.saswat10.instagramclone.components.user.ProfileCard
import timber.log.Timber

@Composable
fun ProfileScreen() {
    val gridState = rememberLazyGridState()
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics

    // Width and height of screen
    val width = displayMetrics.widthPixels
    val height = displayMetrics.heightPixels

    // Device density
    val density = displayMetrics.density

    LazyVerticalGrid(
        state = gridState,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(1.dp),
        columns = GridCells.Adaptive(minSize = 120.dp)
    ) {


        stickyHeader { SimpleHeader("@Username", TextAlign.Center) }
        item(span = { GridItemSpan(maxLineSpan) }) {

            Column(modifier = Modifier.padding(16.dp)) {
                ProfileCard()
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Edit Profile") }
            }
        }
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
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(100.dp))
        }

    }


}