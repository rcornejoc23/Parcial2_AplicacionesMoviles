package com.example.evparcial2.ui.venta

import android.annotation.SuppressLint
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun QrScanner(
    onQrCodeScanned: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val callbackActualizado = rememberUpdatedState(onQrCodeScanned)

    val previewView = remember(context) {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode =
                PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    AndroidView(
        factory = { previewView },
        modifier = modifier.fillMaxSize()
    )

    DisposableEffect(
        context,
        lifecycleOwner,
        previewView
    ) {
        val executor = Executors.newSingleThreadExecutor()

        val opciones = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        val scanner = BarcodeScanning.getClient(opciones)
        val detectado = AtomicBoolean(false)

        val preview = Preview.Builder()
            .build()
            .apply {
                setSurfaceProvider(previewView.surfaceProvider)
            }

        val analisis = ImageAnalysis.Builder()
            .setBackpressureStrategy(
                ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
            )
            .build()

        analisis.setAnalyzer(executor) { imageProxy ->
            if (detectado.get()) {
                imageProxy.close()
                return@setAnalyzer
            }

            val imagen = imageProxy.image

            if (imagen == null) {
                imageProxy.close()
                return@setAnalyzer
            }

            val inputImage = InputImage.fromMediaImage(
                imagen,
                imageProxy.imageInfo.rotationDegrees
            )

            scanner.process(inputImage)
                .addOnSuccessListener { codigos ->
                    val codigoQr = codigos.firstOrNull {
                        it.format == Barcode.FORMAT_QR_CODE &&
                                it.rawValue != null
                    }

                    if (
                        codigoQr != null &&
                        detectado.compareAndSet(false, true)
                    ) {
                        callbackActualizado.value(
                            codigoQr.rawValue!!
                        )
                    }
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }

        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(context)

        var cameraProvider: ProcessCameraProvider? = null

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            cameraProvider?.unbindAll()

            cameraProvider?.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                analisis
            )
        }, ContextCompat.getMainExecutor(context))

        onDispose {
            analisis.clearAnalyzer()
            cameraProvider?.unbindAll()
            scanner.close()
            executor.shutdown()
        }
    }
}
