package com.example.studentapp.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.studentapp.sensor.StepCounterManager
import com.example.studentapp.ui.animation.LoadingAnimation
import com.example.studentapp.ui.animation.WalkingAnimation

@Composable
fun HomeScreen(
    onCameraClick: () -> Unit,
    onMusicClick: () -> Unit,
    onVideoClick: () -> Unit,
    onSensorClick: () -> Unit
) {
    val context = LocalContext.current
    val manager = remember { StepCounterManager(context) }

    DisposableEffect(Unit) {
        manager.start()

        onDispose {
            manager.stop()
        }
    }

    var headerVisible by remember { mutableStateOf(false) }
    var featuresVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        headerVisible = true
        featuresVisible = true
    }

    var expanded by remember { mutableStateOf(false) }

    val cardHeight by animateDpAsState(
        targetValue = if (expanded) 220.dp else 120.dp,
        label = "cardHeight"
    )

    var showMessage by remember { mutableStateOf(false) }

    var selected by remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = selected, label = "cardTransition")

    val scale by transition.animateFloat(label = "scale") { state ->
        if (state) 1.05f else 1f
    }

    val alpha by transition.animateFloat(label = "alpha") { state ->
        if (state) 1f else 0.7f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = manager.steps / 10000f,
        label = "progress"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        AnimatedVisibility(
            visible = headerVisible,
            enter = fadeIn() + slideInVertically()
        ) {
            Text(
                "👋 Hello, Student",
                style = MaterialTheme.typography.headlineLarge
            )
        }

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Today's Activity", style = MaterialTheme.typography.titleMedium)
                if (expanded) {
                    Text("Steps: ${manager.steps}")
                    Text("Goal: 10,000 steps")
                }
            }
        }

        Button(onClick = { expanded = !expanded }) {
            Text(if (expanded) "Collapse" else "Expand")
        }

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(scaleX = scale, scaleY = scale, alpha = alpha)
                .clickable { selected = !selected }
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("${manager.steps} Steps", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = { showMessage = !showMessage }) {
            Text("Show Message")
        }

        AnimatedVisibility(
            visible = showMessage,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "🎉 Keep Going!",
                    modifier = Modifier.padding(20.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        AnimatedVisibility(
            visible = featuresVisible,
            enter = fadeIn() + slideInVertically()
        ) {
            Column {
                Button(
                    onClick = onCameraClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Camera Filter")
                }

                Button(
                    onClick = onMusicClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Music Player")
                }

                Button(
                    onClick = onVideoClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Video Player")
                }

                Button(
                    onClick = onSensorClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Step Counter")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (manager.steps < 1000) {
                LoadingAnimation()
            } else {
                WalkingAnimation()
            }
        }
    }
}
