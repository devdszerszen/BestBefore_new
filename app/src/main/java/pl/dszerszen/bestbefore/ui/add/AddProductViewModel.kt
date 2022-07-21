package pl.dszerszen.bestbefore.ui.add

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.dszerszen.bestbefore.domain.config.ConfigRepository
import pl.dszerszen.bestbefore.ui.add.ScannerStatus.DISABLED
import pl.dszerszen.bestbefore.ui.inapp.RequestPermissionUseCase
import pl.dszerszen.bestbefore.util.Logger
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val logger: Logger,
    private val requestPermissionUseCase: RequestPermissionUseCase,
    private val configRepository: ConfigRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow(AddProductViewState())
    val viewState = _viewState.asStateFlow()

    init {
        if (configRepository.getConfig().isBarcodeScannerEnabled) {
            viewModelScope.launch {
                val isCameraPermissionGranted = requestPermissionUseCase(Manifest.permission.CAMERA)
                _viewState.update {
                    it.copy(
                        scannerAvailable = isCameraPermissionGranted,
                        scannerStatus = if (isCameraPermissionGranted) ScannerStatus.READY_TO_SCAN else DISABLED
                    )
                }
            }
        }
    }

    fun onBarcodeScanned(barcodes: List<String>) {
        if (barcodes.isNotEmpty()) {
            _viewState.update {
                it.copy(
                    barcode = barcodes.first(),
                    scannerStatus = ScannerStatus.SCANNED_SUCCESSFULLY
                )
            }
        }
    }

    fun onBarcodeScannerClosed() {
        _viewState.update { it.copy(scannerStatus = ScannerStatus.DISMISSED) }
    }

    fun reset() {
        _viewState.update { it.copy(barcode = null, scannerStatus = ScannerStatus.READY_TO_SCAN) }
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
    SCANNED_SUCCESSFULLY,
    DISMISSED
}


