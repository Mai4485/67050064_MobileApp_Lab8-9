package com.example.studentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studentapp.ui.camera.CameraScreen
import com.example.studentapp.ui.home.HomeScreen
import com.example.studentapp.ui.music.MusicScreen
import com.example.studentapp.ui.sensor.StepCounterScreen
import com.example.studentapp.ui.theme.StudentAppTheme
import com.example.studentapp.ui.video.VideoScreen

const val HOME = "home"
const val CAMERA = "camera"
const val MUSIC = "music"
const val VIDEO = "video"
const val SENSOR = "sensor"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = HOME,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(HOME) {
                        HomeScreen(
                            onCameraClick = { navController.navigate(CAMERA) },
                            onMusicClick = { navController.navigate(MUSIC) },
                            onVideoClick = { navController.navigate(VIDEO) },
                            onSensorClick = { navController.navigate(SENSOR) }
                        )
                    }
                    composable(
                        CAMERA,
                        enterTransition = { slideInHorizontally { it } },
                        exitTransition = { slideOutHorizontally { it } }
                    ) { CameraScreen() }
                    composable(
                        MUSIC,
                        enterTransition = { fadeIn() },
                        exitTransition = { fadeOut() }
                    ) { MusicScreen() }
                    composable(
                        VIDEO,
                        enterTransition = { scaleIn() },
                        exitTransition = { scaleOut() }
                    ) { VideoScreen() }
                    composable(
                        SENSOR,
                        enterTransition = { slideInHorizontally { it } },
                        exitTransition = { slideOutHorizontally { it } }
                    ) { StepCounterScreen() }
                }
            }
        }
    }
}
