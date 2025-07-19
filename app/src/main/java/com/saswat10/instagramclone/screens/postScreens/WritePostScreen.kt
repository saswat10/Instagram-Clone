package com.saswat10.instagramclone.screens.postScreens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.saswat10.instagramclone.components.posts.Media
import com.saswat10.instagramclone.components.posts.MediaCarousel2
import com.saswat10.instagramclone.components.posts.MediaType
import com.saswat10.instagramclone.components.posts.MediaUri
import com.saswat10.instagramclone.components.posts.mediaList

@Composable
fun WritePostScreen() {

    val context = LocalContext.current


    var imageUris = remember { mutableStateListOf<Media>() }
    var mediaUris = remember { mutableStateListOf<Media>() }
    mediaUris.addAll(mediaList)
    val pickContent =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
            it.mapIndexed { index, uri ->
                val mimeType = getUriMimeType(context, uri)
                if(mimeType?.startsWith("image/") ?: false)
                    mediaUris.add(Media(id = index.toString(), url = uri, type = MediaType.IMAGE))
                else
                    mediaUris.add(Media(id = index.toString(), url = uri, type = MediaType.VIDEO))



            }
        }

    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            it.mapIndexed { index, uri ->
                MediaUri(id = index.toString(), url = uri, type = MediaType.IMAGE)
                imageUris.add(Media(id = index.toString(), url = uri, type = MediaType.IMAGE))
            }

        }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text("Create/Update Post")
        MediaCarousel2(mediaList = mediaUris)
        Button(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = { pickContent.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo)) }) {
            Text("Mix")
        }

    }
}

fun getUriMimeType(context: Context, uri: Uri): String? {
    return context.contentResolver.getType(uri)
}