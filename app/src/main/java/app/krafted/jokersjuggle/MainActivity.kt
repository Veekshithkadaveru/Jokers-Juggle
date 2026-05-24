package app.krafted.jokersjuggle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.krafted.jokersjuggle.ui.GameOverScreen
import app.krafted.jokersjuggle.ui.GameScreen
import app.krafted.jokersjuggle.ui.HomeScreen
import app.krafted.jokersjuggle.ui.LeaderboardScreen
import app.krafted.jokersjuggle.ui.SplashScreen
import app.krafted.jokersjuggle.ui.theme.JokersJuggleTheme
import app.krafted.jokersjuggle.ui.theme.StageDark
import app.krafted.jokersjuggle.viewmodel.GameViewModel
import app.krafted.jokersjuggle.viewmodel.HomeViewModel
import app.krafted.jokersjuggle.viewmodel.LeaderboardViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JokersJuggleTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(StageDark)
                ) { innerPadding ->
                    JokersJuggleApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun JokersJuggleApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {
        composable("splash") {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            val homeVm: HomeViewModel = viewModel()
            LaunchedEffect(Unit) {
                homeVm.loadBestScore()
            }
            HomeScreen(
                viewModel = homeVm,
                onPlayClick = {
                    navController.navigate("game")
                },
                onLeaderboardClick = {
                    navController.navigate("leaderboard")
                }
            )
        }
        composable("game") {
            val gameVm: GameViewModel = viewModel()
            GameScreen(
                vm = gameVm,
                onGameOver = { score, elapsedSeconds, maxObjects ->
                    navController.navigate("game_over/$score/$elapsedSeconds/$maxObjects") {
                        popUpTo("game") { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "game_over/{score}/{time}/{maxObjects}",
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("time") { type = NavType.IntType },
                navArgument("maxObjects") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val gameVm: GameViewModel = viewModel()
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val time = backStackEntry.arguments?.getInt("time") ?: 0
            val maxObjects = backStackEntry.arguments?.getInt("maxObjects") ?: 0
            GameOverScreen(
                score = score,
                timeSurvivedSeconds = time,
                maxObjectsReached = maxObjects,
                onSaveAndReplay = { playerName ->
                    gameVm.saveScore(score, time, maxObjects, playerName)
                    navController.navigate("game") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onSaveAndHome = { playerName ->
                    gameVm.saveScore(score, time, maxObjects, playerName)
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        composable("leaderboard") {
            val leaderboardVm: LeaderboardViewModel = viewModel()
            LaunchedEffect(Unit) {
                leaderboardVm.loadScores()
            }
            LeaderboardScreen(
                viewModel = leaderboardVm,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}