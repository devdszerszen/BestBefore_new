package pl.dszerszen.bestbefore.ui.add

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.dszerszen.bestbefore.R
import pl.dszerszen.bestbefore.ui.barcode.BarcodeScanner

@Composable
fun AddProductScreen(viewModel: AddProductViewModel) {
    val state by viewModel.viewState.collectAsState()
    val scannerViewHeight by animateDpAsState(if (state.canShowScanner()) 256.dp else 0.dp)
    val scannerBorderShape = RoundedCornerShape(24.dp)

    LaunchedEffect(Unit) { viewModel.reset() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BarcodeScanner(
            modifier = Modifier
                .height(scannerViewHeight)
                .clip(scannerBorderShape)
                .border(2.dp, MaterialTheme.colors.primary, scannerBorderShape),
            scannedBarcode = state.barcode,
            barcodeListener = viewModel::onBarcodeScanned
        )
        Spacer(Modifier.height(16.dp))
        if (!state.scannerAvailable) {
            Text(text = stringResource(R.string.no_camera_permission_message), color = Color.Red)
            Spacer(Modifier.height(16.dp))
        }
        Text(text = state.barcode ?: stringResource(R.string.no_barcode))
        Spacer(Modifier.height(16.dp))
        Button(onClick = viewModel::reset) {
            Text(stringResource(R.string.reset))
        }
    }
}