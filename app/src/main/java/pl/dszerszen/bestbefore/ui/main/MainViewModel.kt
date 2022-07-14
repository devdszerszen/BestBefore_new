package pl.dszerszen.bestbefore.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.dszerszen.bestbefore.domain.product.interactor.GetAllProductsUseCase
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.ui.navigation.NavScreen
import pl.dszerszen.bestbefore.ui.navigation.NavigationDispatcher
import pl.dszerszen.bestbefore.util.Logger
import pl.dszerszen.bestbefore.util.Response.Error
import pl.dszerszen.bestbefore.util.Response.Success
import pl.dszerszen.bestbefore.util.StringValue
import pl.dszerszen.bestbefore.util.asImmutable
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val logger: Logger,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val navigationDispatcher: NavigationDispatcher
) : ViewModel() {

    private val _viewState = MutableStateFlow(StartViewState())
    val viewState = _viewState.asImmutable()

    fun initialize() {
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

    fun onButtonClick() {
        navigationDispatcher.navigate(NavScreen.Settings)
    }
}

data class StartViewState(
    val loaderEnabled: Boolean = true,
    val products: List<Product> = emptyList(),
    val errorMessage: StringValue? = null
)