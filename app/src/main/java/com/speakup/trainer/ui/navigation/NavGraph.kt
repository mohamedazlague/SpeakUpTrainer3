package com.speakup.trainer.ui.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.speakup.trainer.ui.history.HistoryScreen
import com.speakup.trainer.ui.home.HomeScreen
import com.speakup.trainer.ui.speaking.SpeakingScreen
import com.speakup.trainer.ui.topic.TopicScreen
import com.speakup.trainer.ui.writing.WritingScreen

/** All navigation routes in the app. */
object Routes {
    const val HOME     = "home"
    const val TOPIC    = "topic/{topicId}/{mode}"
    const val SPEAKING = "speaking/{topicId}"
    const val WRITING  = "writing/{topicId}"
    const val HISTORY  = "history"

    fun topicRoute(topicId: Long, mode: String) = "topic/$topicId/$mode"
    fun speakingRoute(topicId: Long) = "speaking/$topicId"
    fun writingRoute(topicId: Long)  = "writing/$topicId"
}

/**
 * Root navigation graph with slide animations between screens.
 */
@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController   = navController,
        startDestination = Routes.HOME,
        enterTransition  = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition   = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
        popExitTransition  = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
    ) {
        // Home
        composable(Routes.HOME) {
            HomeScreen(
                onTopicGenerated = { topicId, mode ->
                    navController.navigate(Routes.topicRoute(topicId, mode))
                },
                onHistoryClick = { navController.navigate(Routes.HISTORY) }
            )
        }

        // Topic detail (decision screen: speak or write)
        composable(
            route = Routes.TOPIC,
            arguments = listOf(
                navArgument("topicId") { type = NavType.LongType },
                navArgument("mode")    { type = NavType.StringType }
            )
        ) { back ->
            val topicId = back.arguments?.getLong("topicId") ?: 0L
            val mode    = back.arguments?.getString("mode") ?: "SPEAKING"
            TopicScreen(
                topicId  = topicId,
                mode     = mode,
                onStart  = { id, m ->
                    if (m == "SPEAKING") navController.navigate(Routes.speakingRoute(id))
                    else navController.navigate(Routes.writingRoute(id))
                },
                onBack   = { navController.popBackStack() }
            )
        }

        // Speaking practice
        composable(
            route = Routes.SPEAKING,
            arguments = listOf(navArgument("topicId") { type = NavType.LongType })
        ) { back ->
            val topicId = back.arguments?.getLong("topicId") ?: 0L
            SpeakingScreen(
                topicId = topicId,
                onBack  = { navController.popBackStack() },
                onFinished = { navController.navigate(Routes.HISTORY) { popUpTo(Routes.HOME) } }
            )
        }

        // Writing practice
        composable(
            route = Routes.WRITING,
            arguments = listOf(navArgument("topicId") { type = NavType.LongType })
        ) { back ->
            val topicId = back.arguments?.getLong("topicId") ?: 0L
            WritingScreen(
                topicId = topicId,
                onBack  = { navController.popBackStack() },
                onSaved = { navController.navigate(Routes.HISTORY) { popUpTo(Routes.HOME) } }
            )
        }

        // History
        composable(Routes.HISTORY) {
            HistoryScreen(onBack = { navController.popBackStack() })
        }
    }
}
