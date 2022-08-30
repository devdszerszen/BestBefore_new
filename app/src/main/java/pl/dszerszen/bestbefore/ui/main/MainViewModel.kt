package pl.dszerszen.bestbefore.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.dszerszen.bestbefore.domain.product.interactor.AddProductsUseCase
import pl.dszerszen.bestbefore.domain.product.interactor.DeleteProductUseCase
import pl.dszerszen.bestbefore.domain.product.interactor.GetAllProductsUseCase
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.ui.inapp.InAppEventDispatcher
import pl.dszerszen.bestbefore.ui.inapp.InAppMessage
import pl.dszerszen.bestbefore.ui.inapp.navigate
import pl.dszerszen.bestbefore.ui.inapp.showMessage
import pl.dszerszen.bestbefore.ui.navigation.NavScreen
import pl.dszerszen.bestbefore.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val logger: Logger,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val addProductsUseCase: AddProductsUseCase,
    private val inAppEventDispatcher: InAppEventDispatcher,
) : ViewModel() {

    private val _viewState = MutableStateFlow(StartViewState())
    val viewState = _viewState.asImmutable()

    init {
        viewModelScope.launch {
            getAllProductsUseCase().collect { response ->
                when (response) {
                    is Response.Success -> _viewState.update {
                        it.copy(
                            loaderEnabled = false,
                            allProducts = response.data
                        )
                    }
                    is Response.Error -> _viewState.update {
                        it.copy(loaderEnabled = false, errorMessage = response.errorMessage)
                    }
                    is Response.Loading -> TODO()
                }
            }
        }
    }

    fun onUiIntent(intent: MainScreenUiIntent) {
        when (intent) {
            MainScreenUiIntent.OnAddProductClicked -> onAddProductClick()
            is MainScreenUiIntent.OnProductSwiped -> onDelete(intent.product)
            is MainScreenUiIntent.OnRestoreRequested -> onRestore(intent.product)
            is MainScreenUiIntent.OnSearchTextChanged -> onSearch(intent.searchText)
            MainScreenUiIntent.OnSortClicked -> onSortClicked()
        }
    }

    private fun onDelete(product: Product) {
        viewModelScope.launch { deleteProductUseCase(product) }
    }

    private fun onRestore(product: Product) {
        viewModelScope.launch { addProductsUseCase(listOf(product)) }
    }

    private fun onSearch(searchText: String) {
        _viewState.update {
            it.copy(
                searchText = searchText,
                filteredProducts = if (searchText.isEmpty()) {
                    null
                } else {
                    it.allProducts.filter { it.name.contains(searchText, true) }
                }
            )
        }
    }

    private fun onSortClicked() {
        inAppEventDispatcher.showMessage(InAppMessage.Toast("Not implemented yet"))
    }

    private fun onAddProductClick() {
        inAppEventDispatcher.navigate(NavScreen.AddProduct)
    }
}

data class StartViewState(
    val loaderEnabled: Boolean = true,
    val allProducts: List<Product> = emptyList(),
    val filteredProducts: List<Product>? = null,
    val searchText: String = "",
    val errorMessage: StringValue? = null,
    val emptyStateMessage: StringValue = "Add your first project".asStringValue()
) {
    val products = filteredProducts ?: allProducts
    val showEmptyState = allProducts.isEmpty()
}

sealed class MainScreenUiIntent {
    object OnAddProductClicked : MainScreenUiIntent()
    class OnProductSwiped(val product: Product) : MainScreenUiIntent()
    class OnRestoreRequested(val product: Product) : MainScreenUiIntent()
    class OnSearchTextChanged(val searchText: String) : MainScreenUiIntent()
    object OnSortClicked : MainScreenUiIntent()

}