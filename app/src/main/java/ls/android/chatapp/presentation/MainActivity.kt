package ls.android.chatapp.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.google.zxing.integration.android.IntentIntegrator
import ls.android.chatapp.common.Constants
import ls.android.chatapp.common.QRCodeGenerator
import ls.android.chatapp.common.User
import ls.android.chatapp.presentation.qr_code.QRCodeRoute
import ls.android.chatapp.presentation.ui.ChatAppTheme
import java.io.File
import java.io.FileInputStream

class MainActivity : ComponentActivity() {
    private val qrCodeFilePath = Constants.QR_CODE_VALUE
    private val permissionRequestLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value) {
                    permissionGranted = false
                }
            }
            if (!permissionGranted) {
                Toast.makeText(
                    this,
                    "Permission request denied",
                    Toast.LENGTH_SHORT,
                ).show()
            } else {
                startCamera()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storageDir = File(this.filesDir, Constants.QR_CODE)
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        QRCodeGenerator.generateAndSaveQRCode(
            User.id,
            storageDir,
            qrCodeFilePath
        )
        setContent {
            ChatAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val qrCode = loadQRCodeBitmap(storageDir, qrCodeFilePath)
                    QRCodeRoute(
                        modifier = Modifier.fillMaxSize(),
                        qrCode = qrCode
                    )
                }
            }
        }
    }

    private fun onShowQRCodeClick() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }
    }

    private fun onAddClick() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }
    }

    private fun startCamera() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR code")
        integrator.setCameraId(0)
        integrator.setOrientationLocked(false)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    private fun requestPermissions() {
        permissionRequestLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() =
        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                this,
                it
            ) == PackageManager.PERMISSION_GRANTED
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

    companion object {
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
            ).toTypedArray()
    }
}