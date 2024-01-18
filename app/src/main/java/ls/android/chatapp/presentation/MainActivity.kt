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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import ls.android.chatapp.common.Constants
import ls.android.chatapp.common.QRCodeGenerator
import ls.android.chatapp.common.User
import ls.android.chatapp.data.repository.mock.ConnectionsRepositoryMock
import ls.android.chatapp.presentation.connections.ConnectionRoute
import ls.android.chatapp.presentation.connections.ConnectionsViewModel
import ls.android.chatapp.presentation.ui.ChatAppTheme
import java.io.File
import java.io.FileInputStream


class MainActivity : ComponentActivity() {
    private val qrCodeFilePath = Constants.QR_CODE_VALUE
    private val viewModel = ConnectionsViewModel(ConnectionsRepositoryMock())
    private val permissionRequestLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
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
    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        viewModel.addItem(result.contents)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storageDir = File(this.filesDir, Constants.QR_CODE)
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        QRCodeGenerator.generateAndSaveQRCode(
            User.name,
            storageDir,
            qrCodeFilePath
        )
        setContent {
            ChatAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ConnectionRoute(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxSize(),
                        viewModel = viewModel,
                        {},
                        { onAddClick() },
                        { onShowQRCodeClick() })
                }
            }
        }
    }

    private fun onShowQRCodeClick() {
//        todo navigate to qr code screen
    }

    private fun onAddClick() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }
    }

    private fun startCamera() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan a barcode")
        options.setCameraId(0)
        options.setOrientationLocked(false)
        options.setBarcodeImageEnabled(false)
        barcodeLauncher.launch(options)
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