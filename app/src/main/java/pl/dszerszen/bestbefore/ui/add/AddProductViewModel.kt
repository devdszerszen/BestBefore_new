package pl.dszerszen.bestbefore.ui.add

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pl.dszerszen.bestbefore.R
import pl.dszerszen.bestbefore.domain.config.ConfigRepository
import pl.dszerszen.bestbefore.domain.product.interactor.AddProductsUseCase
import pl.dszerszen.bestbefore.domain.product.interactor.GetCategoriesUseCase
import pl.dszerszen.bestbefore.domain.product.model.Category
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.ui.inapp.InAppEventDispatcher
import pl.dszerszen.bestbefore.ui.inapp.navigateBack
import pl.dszerszen.bestbefore.ui.inapp.requestPermission
import pl.dszerszen.bestbefore.util.Logger
import pl.dszerszen.bestbefore.util.ResString
import pl.dszerszen.bestbefore.util.StringValue
import pl.dszerszen.bestbefore.util.nowDate
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val logger: Logger,
    private val inAppEventHandler: InAppEventDispatcher,
    private val configRepository: ConfigRepository,
    private val addProductsUseCase: AddProductsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
) : ViewModel() {
    private val _viewState = MutableStateFlow(AddProductViewState())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            val categories = async { getCategoriesUseCase.invoke().map { it.data }.first() } //TODO consider refactor
            val hasPermission = async {
                if (configRepository.getConfig().isBarcodeScannerEnabled) {
                    inAppEventHandler.requestPermission(Manifest.permission.CAMERA)
                } else false
            }
            _viewState.update {
                it.copy(
                    isInitialized = true,
                    canUseScanner = hasPermission.await(),
                    isDuringScanning = hasPermission.await(),
                    categories = categories.await() ?: emptyList()
                )
            }
        }
    }

    fun onUiIntent(intent: AddProductUiIntent) {
        when (intent) {
            is AddProductUiIntent.BarcodeScanned -> onBarcodeScanned(intent.barcodes)
            is AddProductUiIntent.DateChanged -> onDateSelected(intent.date)
            is AddProductUiIntent.NameChanged -> onNameChanged(intent.name)
            AddProductUiIntent.ScannerClosed -> onBarcodeScannerClosed()
            is AddProductUiIntent.CategoryClicked -> onCategoryClicked(intent.category, intent.checked)
            AddProductUiIntent.SubmitClicked -> onSubmitClicked()
        }
    }

    private fun onBarcodeScanned(barcodes: List<String>) {
        if (barcodes.isNotEmpty()) {
            _viewState.update {
                it.copy(
                    scannedBarcode = barcodes.first(),
                    isDuringScanning = false
                )
            }
        }
    }

    private fun onBarcodeScannerClosed() {
        _viewState.update { it.copy(isDuringScanning = false) }
    }

    private fun onNameChanged(name: String) {
        _viewState.update { it.copy(name = name, nameInputError = null) }
    }

    private fun onDateSelected(date: LocalDate) {
        _viewState.update { it.copy(date = date) }
    }

    private fun onCategoryClicked(clickedCategory: Category, checked: Boolean) {
        _viewState.update {
            it.copy(categories = it.categories.map { category ->
                if (category.id == clickedCategory.id) {
                    category.copy(selected = checked)
                } else category
            })
        }
    }

    private fun onSubmitClicked() {
        //TODO perform proper validation
        with(_viewState.value) {
            if (name.isNotEmpty()) {
                viewModelScope.launch {
                    addProductsUseCase(
                        listOf(
                            Product(
                                name = name,
                                date = date,
                                categories = categories.filter { it.selected }
                            )
                        )
                    )
                    inAppEventHandler.navigateBack()
                }
            } else {
                _viewState.update { it.copy(nameInputError = ResString(R.string.error_empty_input)) }
            }
        }
    }
}

data class AddProductViewState(
    val isInitialized: Boolean = false,
    val canUseScanner: Boolean = false,
    val isDuringScanning: Boolean = false,
    val scannedBarcode: String? = null,
    val name: String = "",
    val nameInputError: StringValue? = null,
    val date: LocalDate = nowDate(),
    val categories: List<Category> = emptyList(),
)

sealed class AddProductUiIntent {
    class BarcodeScanned(val barcodes: List<String>) : AddProductUiIntent()
    class DateChanged(val date: LocalDate) : AddProductUiIntent()
    class NameChanged(val name: String) : AddProductUiIntent()
    object ScannerClosed : AddProductUiIntent()
    class CategoryClicked(val category: Category, val checked: Boolean) : AddProductUiIntent()
    object SubmitClicked : AddProductUiIntent()
}


