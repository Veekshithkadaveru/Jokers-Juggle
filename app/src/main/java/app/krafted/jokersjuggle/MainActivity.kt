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
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.krafted.jokersjuggle.ui.*
import app.krafted.jokersjuggle.ui.theme.JokersJuggleTheme
import app.krafted.jokersjuggle.ui.theme.StageDark

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
            HomeScreen(
                onPlayClick = {
                    navController.navigate("act_intro/1")
                },
                onLeaderboardClick = {
                    navController.navigate("leaderboard")
                }
            )
        }
        composable(
            route = "act_intro/{act}",
            arguments = listOf(navArgument("act") { type = NavType.IntType })
        ) { backStackEntry ->
            val act = backStackEntry.arguments?.getInt("act") ?: 1
            ActIntroScreen(
                act = act,
                onStartAct = { selectedAct ->
                    navController.navigate("game/$selectedAct")
                }
            )
        }
        composable(
            route = "game/{act}",
            arguments = listOf(navArgument("act") { type = NavType.IntType })
        ) { backStackEntry ->
            val act = backStackEntry.arguments?.getInt("act") ?: 1
            GameScreen(
                act = act,
                onActComplete = { completedAct, score ->
                    navController.navigate("act_complete/$completedAct/$score")
                },
                onGameOver = {
                    navController.navigate("game_over")
                }
            )
        }
        composable(
            route = "act_complete/{act}/{score}",
            arguments = listOf(
                navArgument("act") { type = NavType.IntType },
                navArgument("score") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val act = backStackEntry.arguments?.getInt("act") ?: 1
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            ActCompleteScreen(
                act = act,
                score = score,
                onNextActClick = {
                    if (act < 3) {
                        navController.navigate("act_intro/${act + 1}")
                    } else {
                        // All acts complete -> finale
                        navController.navigate("finale") {
                            popUpTo("home") { inclusive = false }
                        }
                    }
                },
                onHomeClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        composable("game_over") {
            GameOverScreen(
                onReplayClick = {
                    navController.navigate("act_intro/1") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onHomeClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        composable("finale") {
            GrandFinaleScreen(
                scores = listOf(0, 0, 0), // Default dummy scores for navigation stub
                onHomeClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        composable("leaderboard") {
            LeaderboardScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}