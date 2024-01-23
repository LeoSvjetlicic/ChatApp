package ls.android.chatapp.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import ls.android.chatapp.common.Constants
import ls.android.chatapp.common.QRCodeGenerator
import ls.android.chatapp.common.User
import ls.android.chatapp.presentation.connections.ConnectionsViewModel
import ls.android.chatapp.presentation.main.MainScreen
import ls.android.chatapp.presentation.ui.ChatAppTheme
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val qrCodeFilePath = Constants.QR_CODE_VALUE
    private lateinit var storageDir: File
    private lateinit var connectionsViewModel: ConnectionsViewModel
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

    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents != null) {
            Log.d("padf",result.contents.toString())
            if (result.contents.startsWith(Constants.QR_CODE_APP_START)) {
                connectionsViewModel.addItem(result.contents.substringAfter('/'))
            } else {
                Toast.makeText(this, "An error accured", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storageDir = File(filesDir, Constants.QR_CODE)
        setContent {
            ChatAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        modifier = Modifier.fillMaxSize(),
                        onAddButtonClick = { onAddClick() },
                        onShowButtonClick = { onShowQRCodeClick() },
                        setConnectionViewModel = { connectionsViewModel = it })
                }
            }
        }
    }

    private fun onShowQRCodeClick() {
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }else{
            storageDir.listFiles()?.forEach { it.deleteRecursively() }
        }
        QRCodeGenerator.generateAndSaveQRCode(
            Constants.QR_CODE_APP_START + User.name,
            storageDir,
            qrCodeFilePath
        )
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

    companion object {
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
            ).toTypedArray()
    }
}