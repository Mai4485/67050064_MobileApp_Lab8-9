package com.example.studentapp.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build()
                preview.surfaceProvider = previewView.surfaceProvider
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
            }, ContextCompat.getMainExecutor(context))
        }
    }

    var filter by remember { mutableStateOf("Original") }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        when (filter) {
            "Warm" -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Yellow.copy(alpha = 0.15f))
                )
            }
            "Cool" -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Blue.copy(alpha = 0.15f))
                )
            }
            "Vintage" -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFA67C52).copy(alpha = 0.25f))
                )
            }
            "Dark" -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.35f))
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.35f))
                .padding(horizontal = 4.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                onClick = { filter = "Original" },
                contentPadding = ButtonDefaults.TextButtonContentPadding
            ) {
                Text("Original", color = Color.White, style = MaterialTheme.typography.labelSmall)
            }
            TextButton(
                onClick = { filter = "Warm" },
                contentPadding = ButtonDefaults.TextButtonContentPadding
            ) {
                Text("Warm", color = Color.White, style = MaterialTheme.typography.labelSmall)
            }
            TextButton(
                onClick = { filter = "Cool" },
                contentPadding = ButtonDefaults.TextButtonContentPadding
            ) {
                Text("Cool", color = Color.White, style = MaterialTheme.typography.labelSmall)
            }
            TextButton(
                onClick = { filter = "Vintage" },
                contentPadding = ButtonDefaults.TextButtonContentPadding
            ) {
                Text("Vintage", color = Color.White, style = MaterialTheme.typography.labelSmall)
            }
            TextButton(
                onClick = { filter = "Dark" },
                contentPadding = ButtonDefaults.TextButtonContentPadding
            ) {
                Text("Dark", color = Color.White, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}