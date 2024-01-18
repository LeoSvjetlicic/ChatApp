package ls.android.chatapp.common

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.File
import java.io.FileOutputStream

object QRCodeGenerator {
    fun generateAndSaveQRCode(content: String, storageDir: File, filePath: String) {
        try {
            val file = File(storageDir, filePath)
            if (file.exists()) {
                return
            }

            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap =
                barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 500, 500)

            saveBitmapToFile(bitmap, storageDir, filePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap, storageDir: File, filePath: String) {
        try {
            val file = File(storageDir, filePath)
            val fileOutputStream = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}