package com.example.expirycheck.barcode

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class BarcodeScanner(
    appContext: Context,
) {
    private val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_ALL_FORMATS
        )
        .build()

    private val scanner: GmsBarcodeScanner = GmsBarcodeScanning.getClient(appContext, options)
    val barcodeResults = MutableStateFlow<String?>(null)

    suspend fun startScan(): String? = withContext(Dispatchers.IO) {
        try {
            val result = Tasks.await(scanner.startScan(), 10, TimeUnit.SECONDS) // Runs in IO thread
            barcodeResults.value = result.rawValue
            Log.d("BarcodeScanner", "Barcode scanned: ${result.rawValue}")
            result.rawValue
        } catch (e: Exception) {
            Log.e("BarcodeScanner", "Barcode scanning failed", e)
            null
        }
    }
}
