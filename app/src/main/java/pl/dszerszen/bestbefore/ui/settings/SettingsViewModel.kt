package pl.dszerszen.bestbefore.ui.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pl.dszerszen.bestbefore.ui.inapp.InAppEventDispatcher
import pl.dszerszen.bestbefore.ui.inapp.navigate
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
        }
    }
}

data class SettingsViewState(
    val message: String = "Hello settings"
)

sealed class SettingsScreenUiIntent {
    object OnGoToCategoriesClicked : SettingsScreenUiIntent()
}
