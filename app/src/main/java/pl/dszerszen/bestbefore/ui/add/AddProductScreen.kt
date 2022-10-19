package pl.dszerszen.bestbefore.ui.add

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import pl.dszerszen.bestbefore.R
import pl.dszerszen.bestbefore.domain.product.model.Category
import pl.dszerszen.bestbefore.ui.add.AddProductUiIntent.*
import pl.dszerszen.bestbefore.ui.barcode.BarcodeScannerCameraPreview
import pl.dszerszen.bestbefore.ui.categories.CategoriesRow
import pl.dszerszen.bestbefore.ui.common.DatePicker
import pl.dszerszen.bestbefore.ui.common.FullScreenLoader
import pl.dszerszen.bestbefore.ui.scanner.ScannerPreviewLayer
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme
import pl.dszerszen.bestbefore.ui.theme.dimens

@Composable
fun AddProductScreen(viewModel: AddProductViewModel = hiltViewModel()) {
    //TODO reset status when enter composition
    val state by viewModel.viewState.collectAsState()
    when {
        state.isInitialized.not() -> {
            FullScreenLoader()
        }
        state.isDuringScanning -> {
            ScannerScreen(viewModel::onUiIntent)
        }
        else -> {
            AddProductScreen(state, viewModel::onUiIntent)
        }
    }
}

@Composable
fun ScannerScreen(onUiIntent: (AddProductUiIntent) -> Unit) {
    ScannerPreviewLayer(
        modifier = Modifier.fillMaxSize(),
        contentBelow = {
            Button(
                modifier = Modifier
                    .padding(dimens.large)
                    .align(Alignment.Center),
                onClick = { onUiIntent(ScannerClosed) }
            ) {
                Text(stringResource(R.string.skip_scan))
            }
        }
    ) {
        BarcodeScannerCameraPreview(onBarcodeScanned = { onUiIntent(BarcodeScanned(it)) })
    }
}

@Composable
private fun AddProductScreen(
    state: AddProductViewState,
    onUiIntent: (AddProductUiIntent) -> Unit
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(200L)
        focusRequester.requestFocus()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(dimens.large)
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val inputError = state.nameInputError?.get()
        if (state.canUseScanner) {
            Text(text = state.scannedBarcode ?: stringResource(R.string.no_barcode))
            Spacer(Modifier.height(dimens.large))
        }
        Spacer(Modifier.height(dimens.large))
        OutlinedTextField(
            modifier = Modifier.focusRequester(focusRequester),
            value = state.name,
            isError = !inputError.isNullOrEmpty(),
            label = { Text("Product name") },
            onValueChange = { onUiIntent(NameChanged(it)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onAny = { focusManager.clearFocus() })
        )
        AnimatedVisibility(!inputError.isNullOrEmpty()) {
            Text(text = inputError.orEmpty(), color = colors.error)
        }
        Spacer(Modifier.height(dimens.large))
        DatePicker {
            focusManager.clearFocus()
            onUiIntent(DateChanged(it))
        }
        Spacer(Modifier.height(dimens.large))
        CategoriesRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimens.medium),
            categories = state.categories,
            onClick = { category, checked ->
                focusManager.clearFocus()
                onUiIntent(CategoryClicked(category, checked))
            }
        )
        Spacer(Modifier.weight(1f))
        Button(onClick = { onUiIntent(SubmitClicked) }) {
            Text(stringResource(R.string.save))
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Scanner - Light theme")
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Scanner - Dark theme")
@Composable
fun ScannerScreenPreview() {
    BestBeforeTheme {
        ScannerScreen(onUiIntent = {})
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Details - Light theme")
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Details - Dark theme")
@Composable
fun AddProductScreenPreview() {
    BestBeforeTheme {
        val state = AddProductViewState(
            categories = List(5) {
                Category(
                    "id",
                    "name ${it + 1}",
                    selected = it == 2
                )
            }
        )
        AddProductScreen(state = state, onUiIntent = {})
    }
}