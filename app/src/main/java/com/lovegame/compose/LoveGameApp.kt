package com.lovegame.compose

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lovegame.compose.login.LoginScreen
import com.lovegame.compose.profile.ProfileScreen
import com.lovegame.compose.terms.Terms
import com.lovegame.domain.util.Resource
import com.lovegame.viewmodels.MainViewModel

@Composable
fun LoveGameApp(
    viewModel: MainViewModel = hiltViewModel()
) {
    val TAG = "LoveGameTag"
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "sign_in") {
        composable("sign_in") {
            val resourceUserData by viewModel.resourceUserData.collectAsStateWithLifecycle()
            val resourceIntentSender by viewModel.resourceIntentSender.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = Unit) {
                viewModel.getUser()
            }
            LaunchedEffect(key1 = resourceUserData) {
                when (resourceUserData) {
                    is Resource.Success -> {
                        Log.d(TAG, "resultGetUser Success")
                        navController.navigate("profile"){
                            popUpTo(navController.graph.id){
                                inclusive = true
                            }
                        }
                        viewModel.resetUserDataResource()
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "resultGetUser Error")
                    }

                    is Resource.Loading -> {
                        Log.d(TAG, "resultGetUser Loading")
                    }
                    is Resource.Empty -> {
                        Log.d(TAG, "resultGetUser Empty")
                    }
                }
            }
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if(result.resultCode == ComponentActivity.RESULT_OK) {
                        viewModel.signInWithIntent(result.data ?: return@rememberLauncherForActivityResult)
                    }
                }
            )
            LaunchedEffect(key1 = resourceIntentSender is Resource.Success) {
                when (resourceIntentSender) {
                    is Resource.Success -> {
                        Log.d(TAG, "resultIntentSender Success")
                        resourceIntentSender.data?.let {intentSender ->
                            launcher.launch(
                                IntentSenderRequest.Builder(intentSender).build()
                            )
                            viewModel.resetIntentSenderResource()
                        } ?: {
                            viewModel.signInGoogle()
                        }
                    }
                    is Resource.Error -> {
                        Log.d(TAG, "resultIntentSender Error")
                    }
                    is Resource.Loading -> {
                        Log.d(TAG, "resultIntentSender Loading")
                    }
                    is Resource.Empty -> {
                        Log.d(TAG, "resultIntentSender Empty")
                    }
                }
            }
            LoginScreen(
                onSignInWithGoogleClick = {
                    viewModel.signInGoogle()
                },
                onCreateAccountClick = {},
                onTermsClick = {
                    navController.navigate("legal/terms")
                },
                onPrivacyClick = {
                    navController.navigate("legal/privacy")
                },
                resourceUserData is Resource.Loading
            )
        }
        composable("profile") {
            ProfileScreen(
                onSignOut = {
                        viewModel.signOut()
                        navController.navigate("sign_in"){
                            popUpTo(navController.graph.id){
                                inclusive = true
                            }
                        }
                }
            )
        }
        composable("legal/{legal_info}",
            arguments = listOf(
                navArgument("legal_info"){
                    type = NavType.StringType
                }
            )) {
            Terms(it.arguments?.getString("legal_info").toString())
        }
    }
}