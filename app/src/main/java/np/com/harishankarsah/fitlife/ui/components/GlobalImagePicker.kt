package np.com.harishankarsah.fitlife.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import np.com.harishankarsah.fitlife.ui.theme.*

@Composable
fun GlobalImagePicker(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    onImagePicked: (Uri?) -> Unit,
    title: String = "Select Image",
    height: Dp = 180.dp,
    shape: Shape = Shapes.small,
    showRemove: Boolean = true,
    backgroundColor: Color = Surface,
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        onImagePicked(uri)
    }

    Column(
        modifier = modifier
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = OnSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(shape)
                .background(backgroundColor)
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {

            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize() ,
                    contentScale = ContentScale.Crop, // ensures the image fills the circle

                )

                if (showRemove) {
                    IconButton(
                        onClick = { onImagePicked(null) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .background(
                                color = Error.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(50)
                            )
                            .size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = OnError
                        )
                    }
                }

            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Pick Image",
                        tint = Primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap to choose image",
                        color = OnSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
