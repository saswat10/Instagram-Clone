package com.saswat10.instagramclone.screens.postScreens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.saswat10.instagramclone.components.posts.MediaCarousel
import com.saswat10.instagramclone.components.posts.MediaType
import com.saswat10.instagramclone.components.posts.MediaUri

@Composable
fun WritePostScreen() {


    var imageUris = remember { mutableStateListOf<MediaUri>() }
    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            it.mapIndexed { index, uri ->
                MediaUri(id = index.toString(), url = uri, type = MediaType.IMAGE)
                imageUris.add(MediaUri(id = index.toString(), url = uri, type = MediaType.IMAGE))
            }

        }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text("Create/Update Post")
        MediaCarousel(mediaList = imageUris)
        Button(modifier = Modifier.align(Alignment.BottomCenter), onClick = {getContent.launch("image/*")}) { }
    }
}