package pl.dszerszen.bestbefore.ui.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pl.dszerszen.bestbefore.ui.inapp.InAppEventDispatcher
import pl.dszerszen.bestbefore.ui.inapp.InAppMessage
import pl.dszerszen.bestbefore.ui.inapp.navigate
import pl.dszerszen.bestbefore.ui.inapp.showMessage
import pl.dszerszen.bestbefore.ui.navigation.NavScreen
import pl.dszerszen.bestbefore.util.Logger
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logger: Logger,
    private val inAppEventDispatcher: InAppEventDispatcher,
) : ViewModel() {
    private val _viewState = MutableStateFlow(SettingsViewState())
    val viewState = _viewState.asStateFlow()

    fun onUiIntent(intent: SettingsScreenUiIntent) {
        when (intent) {
            SettingsScreenUiIntent.OnGoToCategoriesClicked -> inAppEventDispatcher.navigate(NavScreen.Categories)
            is SettingsScreenUiIntent.OnCategoriesToggleClicked -> {
                _viewState.update { it.copy(categoriesEnabled = intent.checked) }
                //TODO implement categories toggle
                inAppEventDispatcher.showMessage(InAppMessage.Toast("Not implemented yet"))
            }
        }
    }
}

data class SettingsViewState(
    val categoriesEnabled: Boolean = false
)

sealed class SettingsScreenUiIntent {
    object OnGoToCategoriesClicked : SettingsScreenUiIntent()
    class OnCategoriesToggleClicked(val checked: Boolean) : SettingsScreenUiIntent()
}
