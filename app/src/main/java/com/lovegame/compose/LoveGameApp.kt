package com.lovegame.compose

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lovegame.compose.createaccount.CreateAccount
import com.lovegame.compose.login.LoginScreen
import com.lovegame.compose.profile.ProfileScreen
import com.lovegame.compose.terms.Terms

@Composable
fun LoveGameApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login",
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start, tween(300)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300, easing = LinearEasing))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End, tween(300)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(300, easing = LinearEasing))
        }) {
        composable("login") {

            LoginScreen(
                onCreateAccountClick = {
                    navController.navigate("create_account")
                },
                onTermsClick = {
                    navController.navigate("legal/terms")
                },
                onPrivacyClick = {
                    navController.navigate("legal/privacy")
                },
                gotoProfile = {
                    navController.navigate("profile") {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("create_account") {
            CreateAccount(
                onTermsClick = {
                    navController.navigate("legal/terms")
                },
                onPrivacyClick = {
                    navController.navigate("legal/privacy")
                },
                gotoLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                gotoLogin = {
                    navController.navigate("login") {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("legal/{legal_info}",
            arguments = listOf(
                navArgument("legal_info") {
                    type = NavType.StringType
                }
            )) {
            Terms(it.arguments?.getString("legal_info").toString())
        }
    }
}