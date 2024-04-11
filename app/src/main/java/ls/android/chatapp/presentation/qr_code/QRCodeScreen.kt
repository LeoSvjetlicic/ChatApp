package ls.android.chatapp.presentation.qr_code

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QRCodeRoute(viewModel: QRCodeViewModel) {
    QRCodeScreen(modifier = Modifier, qrCode = viewModel.bitmap)
}

@Composable
fun QRCodeScreen(modifier: Modifier, qrCode: Bitmap?) {
    var scale by remember {
        mutableFloatStateOf(1F)
    }
    Box(modifier = modifier) {
        val state = rememberTransformableState { zoomChange, _, _ ->
            scale = (scale * zoomChange).coerceIn(1F, 2F)
        }
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp),
            text = "Your QR code",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        if (qrCode != null) {
            Image(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .transformable(state),
                bitmap = qrCode.asImageBitmap(),
                contentDescription = "QR code"
            )
        }
    }
}
