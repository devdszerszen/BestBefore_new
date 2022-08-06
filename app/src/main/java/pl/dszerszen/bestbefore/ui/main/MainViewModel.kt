package pl.dszerszen.bestbefore.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.dszerszen.bestbefore.domain.product.interactor.DeleteProductUseCase
import pl.dszerszen.bestbefore.domain.product.interactor.GetAllProductsUseCase
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.ui.inapp.InAppEventDispatcher
import pl.dszerszen.bestbefore.ui.inapp.navigate
import pl.dszerszen.bestbefore.ui.navigation.NavScreen
import pl.dszerszen.bestbefore.util.Logger
import pl.dszerszen.bestbefore.util.Response
import pl.dszerszen.bestbefore.util.StringValue
import pl.dszerszen.bestbefore.util.asImmutable
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val logger: Logger,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
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
                            products = response.data
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
        }
    }

    private fun onDelete(product: Product) {
        viewModelScope.launch { deleteProductUseCase(product) }
    }

    private fun onAddProductClick() {
        inAppEventDispatcher.navigate(NavScreen.AddProduct)
    }
}

data class StartViewState(
    val loaderEnabled: Boolean = true,
    val products: List<Product> = emptyList(),
    val errorMessage: StringValue? = null
)

sealed class MainScreenUiIntent {
    object OnAddProductClicked : MainScreenUiIntent()
    class OnProductSwiped(val product: Product) : MainScreenUiIntent()

}