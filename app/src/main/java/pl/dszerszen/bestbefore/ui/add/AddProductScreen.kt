package pl.dszerszen.bestbefore.ui.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.dszerszen.bestbefore.ui.barcode.BarcodeScanner

@Composable
fun AddProductScreen(viewModel: AddProductViewModel) {
    val state by viewModel.viewState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(state.scannerAvailable) {
            BarcodeScanner(
                modifier = Modifier.height(256.dp),
                scannedBarcode = state.barcode,
                barcodeListener = viewModel::onBarcodeScanned
            )
            Spacer(Modifier.height(16.dp))
        }
        Text(text = state.barcode ?: "No barcode scanned")
        Spacer(Modifier.height(16.dp))
        Button(onClick = viewModel::reset) {
            Text("Reset")
        }
    }
}