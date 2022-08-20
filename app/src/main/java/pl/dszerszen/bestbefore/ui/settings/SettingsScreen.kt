package pl.dszerszen.bestbefore.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import pl.dszerszen.bestbefore.ui.settings.SettingsScreenUiIntent.OnGoToCategoriesClicked
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme
import pl.dszerszen.bestbefore.ui.theme.dimens

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.viewState.collectAsState()
    SettingsScreen(state, viewModel::onUiIntent)
}

@Composable
fun SettingsScreen(
    state: SettingsViewState,
    onUiIntent: (SettingsScreenUiIntent) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(dimens.medium)
    ) {
        TextButton(onClick = { onUiIntent(OnGoToCategoriesClicked) }) {
            Text("Manage categories")
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