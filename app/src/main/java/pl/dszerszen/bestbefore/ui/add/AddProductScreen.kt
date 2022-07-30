package pl.dszerszen.bestbefore.ui.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.dszerszen.bestbefore.R
import pl.dszerszen.bestbefore.ui.barcode.BarcodeScanner
import pl.dszerszen.bestbefore.ui.theme.dimens

@Composable
fun AddProductScreen(viewModel: AddProductViewModel) {
    val state by viewModel.viewState.collectAsState()
    val scannerBorderShape = RoundedCornerShape(24.dp)

    LaunchedEffect(Unit) { viewModel.reset() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimens.large),
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
                onBarcodeClosed = viewModel::onBarcodeScannerClosed,
                onBarcodeScanned = viewModel::onBarcodeScanned
            )
        }
        Spacer(Modifier.height(dimens.large))
        Text(text = state.barcode ?: stringResource(R.string.no_barcode))
        Spacer(Modifier.height(dimens.large))
        Button(onClick = viewModel::reset) {
            Text(stringResource(R.string.reset))
        }
    }
}