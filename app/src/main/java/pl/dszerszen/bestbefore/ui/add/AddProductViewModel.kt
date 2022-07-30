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
import pl.dszerszen.bestbefore.ui.add.ScannerStatus.ACTIVE
import pl.dszerszen.bestbefore.ui.add.ScannerStatus.DISABLED
import pl.dszerszen.bestbefore.ui.inapp.InAppEventDispatcher
import pl.dszerszen.bestbefore.ui.inapp.requestPermission
import pl.dszerszen.bestbefore.util.Logger
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val logger: Logger,
    private val inAppEventHandler: InAppEventDispatcher,
    private val configRepository: ConfigRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow(AddProductViewState())
    val viewState = _viewState.asStateFlow()

    private var scannerEnabled = false

    init {
        if (configRepository.getConfig().isBarcodeScannerEnabled) {
            viewModelScope.launch {
                scannerEnabled = inAppEventHandler.requestPermission(Manifest.permission.CAMERA)
                _viewState.update {
                    it.copy(
                        scannerStatus = resolveScannerStatus()
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
                    scannerStatus = ScannerStatus.SUCCESS
                )
            }
        }
    }

    fun onBarcodeScannerClosed() {
        _viewState.update { it.copy(scannerStatus = ScannerStatus.DISMISSED) }
    }

    fun reset() {
        _viewState.update { it.copy(barcode = null, scannerStatus = resolveScannerStatus() ) }
    }

    private fun resolveScannerStatus(): ScannerStatus = if (scannerEnabled) ACTIVE else DISABLED
}

data class AddProductViewState(
    val barcode: String? = null,
    val scannerStatus: ScannerStatus = DISABLED
) {
    val scannerEnabled = scannerStatus.enabled
}

enum class ScannerStatus(val enabled: Boolean = false) {
    DISABLED,
    DISMISSED,
    ACTIVE(true),
    SUCCESS
}


