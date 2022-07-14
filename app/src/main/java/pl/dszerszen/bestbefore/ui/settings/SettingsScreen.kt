package pl.dszerszen.bestbefore.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Cyan

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val state by viewModel.viewState.collectAsState()

    Box(modifier = Modifier.background(Cyan)) {
        Text(text = state.message)
    }
}