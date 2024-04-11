package ls.android.chatapp.presentation.qr_code

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ls.android.chatapp.common.Constants
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

@HiltViewModel
class QRCodeViewModel @Inject constructor(app: Application) : ViewModel() {
    var bitmap: Bitmap? = null

    init {
        val qrCodeFilePath = Constants.QR_CODE_VALUE
        val storageDir = File(app.filesDir, Constants.QR_CODE)
        bitmap = loadQRCodeBitmap(storageDir, qrCodeFilePath)
    }

    private fun loadQRCodeBitmap(storageDir: File, filePath: String): Bitmap? {
        try {
            val file = File(storageDir, filePath)

            if (!file.exists()) {
                return null
            }

            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.RGB_565

            val inputStream = FileInputStream(file)

            return BitmapFactory.decodeStream(inputStream, null, options)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
