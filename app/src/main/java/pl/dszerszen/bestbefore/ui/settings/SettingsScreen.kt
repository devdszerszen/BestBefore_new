package pl.dszerszen.bestbefore.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import pl.dszerszen.bestbefore.R
import pl.dszerszen.bestbefore.ui.settings.SettingsScreenUiIntent.OnCategoriesToggleClicked
import pl.dszerszen.bestbefore.ui.settings.SettingsScreenUiIntent.OnGoToCategoriesClicked
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme
import pl.dszerszen.bestbefore.ui.theme.dimens
import pl.dszerszen.bestbefore.util.ResString

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.viewState.collectAsState()
    SettingsScreen(state, viewModel::onUiIntent)
}

@Composable
private fun SettingsScreen(
    state: SettingsViewState,
    onUiIntent: (SettingsScreenUiIntent) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(dimens.medium)
    ) {
        SettingsSection(ResString(R.string.categories_settings_header)) {
            SettingsToggle(ResString(R.string.categories_toggle_label), state.categoriesEnabled) {
                onUiIntent(OnCategoriesToggleClicked(it))
            }
            SettingsRedirectButton(ResString(R.string.categories_manage_label)) {
                onUiIntent(OnGoToCategoriesClicked)
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    BestBeforeTheme {
        val state = SettingsViewState()
        SettingsScreen(state) {}
    }
}