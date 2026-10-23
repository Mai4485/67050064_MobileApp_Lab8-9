package com.example.studentapp.ui.sensor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.studentapp.sensor.StepCounterManager

@Composable
fun StepCounterScreen() {
    val context = LocalContext.current

    val manager = remember {
        StepCounterManager(context)
    }

    DisposableEffect(Unit) {
        manager.start()

        onDispose {
            manager.stop()
        }
    }

    val progress = manager.steps / 10000f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Today's Steps",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            "${manager.steps}",
            style = MaterialTheme.typography.displayLarge
        )

        Text("steps")

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
