package pl.dszerszen.bestbefore.ui.barcode

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import pl.dszerszen.bestbefore.util.DebugLogger
import pl.dszerszen.bestbefore.util.Logger

class BarcodeAnalyser(
    private val logger: Logger? = DebugLogger,
    private val onBarcodeDetected: (List<Barcode>) -> Unit
) : ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val image = imageProxy.image
        if (image != null) {
            val input = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)

            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_EAN_13)
                .build()

            val scanner = BarcodeScanning.getClient(options)

            scanner.process(input)
                .addOnCompleteListener {
                    if (it.isSuccessful && it.result.isNotEmpty()) {
                        DebugLogger.log("--Barcode-- found some barcodes")
                        onBarcodeDetected(it.result)
                    } else {
                        DebugLogger.log("--Barcode-- finished with NO barcodes detected")
                    }
                    imageProxy.close()
                }
                .addOnFailureListener {
                    DebugLogger.log("--Barcode-- finished with error: ${it.message}")
                    imageProxy.close()
                }
        }
    }

//    override fun analyze(image: ImageProxy) {
//        val currentTimestamp = System.currentTimeMillis()
//        if (currentTimestamp - lastAnalyzedImageTimeStamp >= TimeUnit.SECONDS.toMillis(1)) {
//            image.image?.let { imageToAnalyze ->
//                val options = BarcodeScannerOptions.Builder()
//                    .setBarcodeFormats(Barcode.FORMAT_EAN_13, Barcode.FORMAT_EAN_8)
//                    .build()
//                val barcodeScanner = BarcodeScanning.getClient(options)
//                val imageToProcess = InputImage.fromMediaImage(imageToAnalyze, image.imageInfo.rotationDegrees)
//
//                barcodeScanner.process(imageToProcess)
//                    .addOnSuccessListener { barcodes ->
//                        if (barcodes.isNotEmpty()) {
//                            logger?.log("ANALYSER >> Detected ${barcodes.size} barcodes")
//                            onBarcodeDetected(barcodes)
//                        } else {
//                            logger?.log("ANALYSER >> No barcodes detected")
//                        }
//                    }
//                    .addOnFailureListener { exception ->
//                        logger?.log("ANALYSER >> Error occured: ${exception.localizedMessage}")
//                    }
//                    .addOnCompleteListener {
//                        logger?.log("ANALYSER >> Task completed")
//                        image.close()
//                    }
//            }
//            lastAnalyzedImageTimeStamp = currentTimestamp
//        } else {
//            image.close()
//        }
//    }
}