package pl.dszerszen.bestbefore.ui.barcode

import android.content.Context
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.launch
import pl.dszerszen.bestbefore.util.DebugLogger
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun BarcodeScanner(
    modifier: Modifier = Modifier,
    scannedBarcode: String? = null,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    barcodeListener: (List<Barcode>) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    //Used to unbind camera on dispose view. It does not work out of the box
    var cameraUnbinder = remember { {} }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            cameraUnbinder()
        }
    }
    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                val previewView = PreviewView(context).apply {
                    this.scaleType = scaleType
                }

                val previewUseCase = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val barcodeAnalyserUseCase = ImageAnalysis.Builder()
                    .setTargetResolution(Size(1280, 720))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .apply { setAnalyzer(context.executor, BarcodeAnalyser(onBarcodeDetected = barcodeListener)) }

                coroutineScope.launch {
                    val cameraProvider = context.getCameraProvider().also {
                        cameraUnbinder = { it.unbindAll() }
                    }
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            previewUseCase,
                            barcodeAnalyserUseCase
                        )
                    } catch (e: Exception) {
                        DebugLogger.log("Error during camera binding: ${e.message}")
                    }
                }
                previewView
            }
        )
        AnimatedLine(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            barcode = scannedBarcode
        )
    }
}

@Composable
fun AnimatedLine(modifier: Modifier, barcode: String?) {
    val transition = rememberInfiniteTransition()
    val alpha by transition.animateFloat(0f, 1f, infiniteRepeatable(tween(1000), RepeatMode.Reverse))
    val color by animateColorAsState(if (barcode != null) Color.Green else Color.Red)

    Divider(modifier = modifier.alpha(alpha), color = color, thickness = 2.dp)
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { cont ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener({
            cont.resume(future.get())
        }, executor)
    }
}

internal val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)