package com.saswat10.instagramclone.presentation.screens.postScreens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddToPhotos
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.SmartDisplay
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.AddToPhotos
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.SmartDisplay
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material.icons.rounded.VideoStable
import androidx.compose.material.icons.twotone.AddPhotoAlternate
import androidx.compose.material.icons.twotone.AddToPhotos
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.SmartDisplay
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saswat10.instagramclone.presentation.components.common.SimpleHeader
import com.saswat10.instagramclone.presentation.components.posts.MediaCarousel2
import com.saswat10.instagramclone.presentation.components.user.ImageSizes
import com.saswat10.instagramclone.viewmodels.CreatePostViewModel

@Composable
fun CreatePostScreen(viewModel: CreatePostViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val pickContent =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
            it.mapIndexed { index, uri ->
                val mimeType = viewModel.getUriMimeType(context, uri)
                val media = viewModel.mimeToMedia(mimeType, index, uri)
                viewModel.addMedia(media, uri)
            }
        }

    val pickVideo =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if(it!=null) {
                val mimeType = viewModel.getUriMimeType(context, it)
                val media = viewModel.mimeToMedia(mimeType, 0, it)
                viewModel.addMedia(media, it)
            }
        }



    Box() {


        Column(
            modifier = Modifier
                .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SimpleHeader(title = "Create New Post")
            TextField(
                value = uiState.caption,
                onValueChange = viewModel::addCaption,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                minLines = 3,
                placeholder = { Text("Add Caption") },
            )
            if(uiState.media.isEmpty()) EmptyCarousel()
            MediaCarousel2(uiState.media, viewModel::removeMedia, true)


        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomStart),
            horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {


            TextButton(onClick = viewModel::clearAllMedia) {
                Icon(Icons.TwoTone.Delete, "", Modifier.size(ImageSizes.SMALL), tint = MaterialTheme.colorScheme.error)
            }
            TextButton(onClick = { pickContent.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
                Icon(Icons.TwoTone.AddToPhotos, "", Modifier.size(ImageSizes.SMALL))
            }
            TextButton(onClick = { pickVideo.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.VideoOnly)) }) {
                Icon(Icons.TwoTone.SmartDisplay, "", Modifier.size(ImageSizes.SMALL))
            }
            Button(onClick = viewModel::share, enabled = !uiState.media.isEmpty()) { Text("Share") }
        }

    }

}

@Composable
fun EmptyCarousel() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(7 / 8f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Rounded.AddToPhotos, "", Modifier.size(80.dp))
        Spacer(Modifier.height(10.dp))
        Text("Start Adding Media", textAlign = TextAlign.Center)
    }
}