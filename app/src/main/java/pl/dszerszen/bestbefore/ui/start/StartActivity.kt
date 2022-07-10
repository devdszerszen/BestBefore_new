package pl.dszerszen.bestbefore.ui.start

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme

@AndroidEntryPoint
class StartActivity : ComponentActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<StartViewModel>()
        requestCameraPermission(viewModel::onCameraPermissionResult)
        setContent {
            viewModel.loadData()
            BestBeforeTheme {
                StartScreen(viewModel)
            }
        }
    }

    private fun requestCameraPermission(onPermissionResult: (Boolean) -> Unit) {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            onPermissionResult(it)
        }
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }
}