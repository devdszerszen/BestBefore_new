package pl.dszerszen.bestbefore.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.dszerszen.bestbefore.domain.product.interactor.GetAllProductsUseCase
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.util.Logger
import pl.dszerszen.bestbefore.util.Response.Error
import pl.dszerszen.bestbefore.util.Response.Success
import pl.dszerszen.bestbefore.util.StringValue
import pl.dszerszen.bestbefore.util.asImmutable
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val logger: Logger,
    private val getAllProductsUseCase: GetAllProductsUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(StartViewState())
    val viewState = _viewState.asImmutable()

    fun loadData() {
        viewModelScope.launch {
            getAllProductsUseCase().let { response ->
                when (response) {
                    is Success -> _viewState.update {
                        it.copy(
                            loaderEnabled = false,
                            products = response.data
                        )
                    }
                    is Error -> _viewState.update {
                        it.copy(loaderEnabled = false, errorMessage = response.errorMessage)
                    }
                }
            }
        }
    }

    fun onCameraPermissionResult(hasAccess: Boolean) {
        _viewState.update { it.copy(hasCameraPermission = hasAccess) }
    }
}

data class StartViewState(
    val loaderEnabled: Boolean = true,
    val products: List<Product> = emptyList(),
    val errorMessage: StringValue? = null,
    val hasCameraPermission: Boolean = false
)