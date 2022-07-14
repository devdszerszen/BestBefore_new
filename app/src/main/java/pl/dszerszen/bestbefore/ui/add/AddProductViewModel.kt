package pl.dszerszen.bestbefore.ui.add

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.dszerszen.bestbefore.ui.add.ScannerStatus.*
import pl.dszerszen.bestbefore.ui.inapp.RequestPermissionUseCase
import pl.dszerszen.bestbefore.util.Logger
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val logger: Logger,
    private val requestPermissionUseCase: RequestPermissionUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow(AddProductViewState())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            val isCameraPermissionGranted = requestPermissionUseCase(Manifest.permission.CAMERA)
            _viewState.update { it.copy(scannerAvailable = isCameraPermissionGranted, scannerStatus = READY_TO_SCAN) }
        }
    }

    fun onBarcodeScanned(barcodes: List<Barcode>) {
        if (barcodes.isNotEmpty()) {
            _viewState.update { it.copy(barcode = barcodes.first().rawValue, scannerStatus = SCANNED_SUCCESSFULLY) }
        }
    }

    fun reset() {
        _viewState.update { it.copy(barcode = null, scannerStatus = READY_TO_SCAN) }
    }
}

data class AddProductViewState(
    val scannerAvailable: Boolean = false,
    val barcode: String? = null,
    val scannerStatus: ScannerStatus = DISABLED
) {
    fun canShowScanner() = scannerAvailable && scannerStatus.enabled
}

enum class ScannerStatus(val enabled: Boolean = false) {
    DISABLED,
    READY_TO_SCAN(true),
    SCANNED_SUCCESSFULLY
}


