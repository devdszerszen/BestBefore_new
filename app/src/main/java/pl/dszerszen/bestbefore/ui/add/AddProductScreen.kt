package pl.dszerszen.bestbefore.ui.add

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.dszerszen.bestbefore.R
import pl.dszerszen.bestbefore.ui.add.AddProductUiIntent.*
import pl.dszerszen.bestbefore.ui.barcode.BarcodeScanner
import pl.dszerszen.bestbefore.ui.common.DatePicker
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme
import pl.dszerszen.bestbefore.ui.theme.dimens

@Composable
fun AddProductScreen(viewModel: AddProductViewModel) {
    val state by viewModel.viewState.collectAsState()
    AddProductScreen(state, viewModel::onUiIntent)
}

@Composable
private fun AddProductScreen(
    state: AddProductViewState,
    onIntent: (AddProductUiIntent) -> Unit
) {
    val scannerBorderShape = RoundedCornerShape(24.dp)
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) { onIntent(ResetClicked) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(dimens.large)
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = state.scannerEnabled,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            BarcodeScanner(
                modifier = Modifier
                    .height(256.dp)
                    .clip(scannerBorderShape)
                    .border(dimens.small, MaterialTheme.colors.primary, scannerBorderShape),
                scannedBarcode = state.barcode,
                onBarcodeClosed = { onIntent(ScannerClosed) },
                onBarcodeScanned = { onIntent(BarcodeScanned(it)) }
            )
        }
        Spacer(Modifier.height(dimens.large))
        Text(text = state.barcode ?: stringResource(R.string.no_barcode))
        Spacer(Modifier.height(dimens.large))
        Button(onClick = { onIntent(ResetClicked) }) {
            Text(stringResource(R.string.reset))
        }
        Spacer(Modifier.height(dimens.large))
        OutlinedTextField(
            value = state.name,
            onValueChange = { onIntent(NameChanged(it)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onAny = { focusManager.clearFocus() })
        )
        Spacer(Modifier.height(dimens.large))
        DatePicker { onIntent(DateChanged(it)) }
        Spacer(Modifier.weight(1f))
        Button(onClick = { onIntent(SubmitClicked) }) {
            Text(stringResource(R.string.save))
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
@Composable
fun AddProductScreenPreview() {
    BestBeforeTheme {
        AddProductScreen(state = AddProductViewState(), onIntent = {})
    }
}