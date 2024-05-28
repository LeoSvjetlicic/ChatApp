package ls.android.chatapp.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import ls.android.chatapp.common.Constants
import ls.android.chatapp.common.QRCodeGenerator
import ls.android.chatapp.data.repository.real.ConnectionRepositoryImpl
import ls.android.chatapp.presentation.chat.ChatViewModel
import ls.android.chatapp.presentation.connections.ConnectionsViewModel
import ls.android.chatapp.presentation.main.MainScreen
import ls.android.chatapp.presentation.ui.ChatAppTheme
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repository: ConnectionRepositoryImpl
    private var isChatOpen: Boolean = false
    private val qrCodeFilePath = Constants.QR_CODE_VALUE
    private lateinit var storageDir: File
    private lateinit var connectionsViewModel: ConnectionsViewModel
    private lateinit var chatViewModel: ChatViewModel
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
            if (result.contents.startsWith(Constants.QR_CODE_APP_START)) {
                connectionsViewModel.addItem(result.contents.substringAfter('/'))
            } else {
                Toast.makeText(this, "An error appeared", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (isChatOpen) {
            chatViewModel.updateConnectionStatus(false)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isChatOpen) {
            chatViewModel.updateConnectionStatus(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermissions()
        storageDir = File(filesDir, Constants.QR_CODE)
        setContent {
            ChatAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        modifier = Modifier.fillMaxSize(),
                        viewModel = hiltViewModel(),
                        isLoggedIn = Firebase.auth.currentUser!=null,
                        onAddButtonClick = { onAddClick() },
                        onShowButtonClick = { onShowQRCodeClick() },
                        toggleIsChatVisible = {
                            isChatOpen = it
                        },
                        setConnectionViewModel = { connectionsViewModel = it },
                        setMessageViewModel = { chatViewModel = it })
                }
            }
        }
    }

    private fun requestNotificationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }

    private fun onShowQRCodeClick() {
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        } else {
            storageDir.listFiles()?.forEach { it.deleteRecursively() }
        }
        QRCodeGenerator.generateAndSaveQRCode(
            Constants.QR_CODE_APP_START + Firebase.auth.currentUser?.email.toString(),
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
