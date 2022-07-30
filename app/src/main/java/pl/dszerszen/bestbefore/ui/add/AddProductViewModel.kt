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
import pl.dszerszen.bestbefore.ui.inapp.InAppEventDispatcher
import pl.dszerszen.bestbefore.ui.inapp.requestPermission
import pl.dszerszen.bestbefore.util.Logger
import pl.dszerszen.bestbefore.util.nowDate
import java.time.LocalDate
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

    fun onUiIntent(intent: AddProductUiIntent) {
        when (intent) {
            is AddProductUiIntent.BarcodeScanned -> onBarcodeScanned(intent.barcodes)
            is AddProductUiIntent.DateChanged -> onDateSelected(intent.date)
            is AddProductUiIntent.NameChanged -> onNameChanged(intent.name)
            AddProductUiIntent.ResetClicked -> reset()
            AddProductUiIntent.ScannerClosed -> onBarcodeScannerClosed()
            AddProductUiIntent.SubmitClicked -> onSubmitClicked()
        }
    }

    private fun onBarcodeScanned(barcodes: List<String>) {
        if (barcodes.isNotEmpty()) {
            _viewState.update {
                it.copy(
                    barcode = barcodes.first(),
                    scannerStatus = ScannerStatus.SUCCESS
                )
            }
        }
    }

    private fun onBarcodeScannerClosed() {
        _viewState.update { it.copy(scannerStatus = ScannerStatus.DISMISSED) }
    }

    private fun reset() {
        _viewState.update { it.copy(barcode = null, scannerStatus = resolveScannerStatus()) }
    }

    private fun onNameChanged(name: String) {
        _viewState.update { it.copy(name = name) }
    }

    private fun onDateSelected(date: LocalDate) {
        _viewState.update { it.copy(date = date) }
    }

    private fun onSubmitClicked() {
        with(_viewState.value) {
            logger.log(">> onSubmitClicked")
            logger.log("Name: $name")
            logger.log("Date: $date")
        }
    }

    private fun resolveScannerStatus(): ScannerStatus = if (scannerEnabled) ScannerStatus.ACTIVE else DISABLED
}

data class AddProductViewState(
    val barcode: String? = null,
    val name: String = "",
    val date: LocalDate = nowDate(),
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

sealed class AddProductUiIntent{
    class BarcodeScanned(val barcodes: List<String>) : AddProductUiIntent()
    class DateChanged(val date: LocalDate) : AddProductUiIntent()
    class NameChanged(val name: String) : AddProductUiIntent()
    object ScannerClosed : AddProductUiIntent()
    object ResetClicked : AddProductUiIntent()
    object SubmitClicked : AddProductUiIntent()
}


