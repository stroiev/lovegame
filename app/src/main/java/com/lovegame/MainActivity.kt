package com.lovegame

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lovegame.domain.util.Resource
import com.lovegame.presentation.profile.ProfileScreen
import com.lovegame.presentation.sign_in.SignInScreen
import com.lovegame.ui.theme.LoveGameTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    private val viewModel: MainViewModel by viewModels()
//    private lateinit var auth: FirebaseAuth
//    private lateinit var googleSignInClient: GoogleSignInClient

//    private val getResult =
//        registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()) {
//            if(it.resultCode == Activity.RESULT_OK){
//                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
//                try {
//                    // Google Sign In was successful, authenticate with Firebase
//                    val account = task.getResult(ApiException::class.java)!!
//                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
//                    firebaseAuthWithGoogle(account.idToken!!)
//                } catch (e: ApiException) {
//                    // Google Sign In failed, update UI appropriately
//                    Log.w(TAG, "Google sign in failed", e)
//                }
//            }
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//        auth = Firebase.auth

        installSplashScreen().setKeepOnScreenCondition {
            viewModel.isLoading.value
        }

//        setMainContent(displayWidth, displayHeight, "")

        setContent {
            LoveGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "sign_in") {
                        composable("sign_in") {
                            val resourceUserData by viewModel.resourceUserData.collectAsStateWithLifecycle()

                            LaunchedEffect(key1 = Unit) {
                                viewModel.getUser()
                            }

                            LaunchedEffect(key1 = resourceUserData is Resource.Success) {
                                when (resourceUserData) {
                                    is Resource.Success -> {
                                        Log.d(TAG, "resultGetUser Success")
                                        Toast.makeText(applicationContext, "Sign in successful", Toast.LENGTH_SHORT).show()
                                        navController.navigate("profile")
                                        viewModel.resetUserDataResource()
                                    }

                                    is Resource.Error -> {
                                        Log.d(TAG, "resultGetUser Error")
                                        Toast.makeText(applicationContext, resourceUserData.message, Toast.LENGTH_SHORT).show()
                                    }

                                    is Resource.Loading -> {
                                        Log.d(TAG, "resultGetUser Loading")
                                        Toast.makeText(applicationContext, "resultGetUser Loading", Toast.LENGTH_SHORT).show()
                                    }
                                    is Resource.Empty -> {
                                        Log.d(TAG, "resultGetUser Empty")
                                        Toast.makeText(applicationContext, "resultGetUser Empty", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if(result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            viewModel.signInWithIntent(result.data ?: return@launch)
                                        }
                                    }
                                }
                            )

                            SignInScreen(
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        viewModel.signIn()
                                        viewModel.resourceIntentSender.collect { resource ->
                                            when (resource) {
                                                is Resource.Success -> {
                                                    Log.d(TAG, "resultIntentSender Success")
                                                    Toast.makeText(applicationContext, "resultIntentSender Success", Toast.LENGTH_SHORT).show()
                                                    resource.data?.let {intentSender ->
                                                        launcher.launch(
                                                            IntentSenderRequest.Builder(intentSender).build()
                                                        )
                                                    } ?: {
                                                        viewModel.signIn()
                                                    }
                                                }
                                                is Resource.Error -> {
                                                    Log.d(TAG, "resultIntentSender Error")
                                                    Toast.makeText(applicationContext, "resultIntentSender Error", Toast.LENGTH_SHORT).show()
                                                }
                                                is Resource.Loading -> {
                                                    Log.d(TAG, "resultIntentSender Loading")
                                                    Toast.makeText(applicationContext, "resultIntentSender Loading", Toast.LENGTH_SHORT).show()
                                                }

                                                else -> {}
                                            }
                                        }
                                    }
                                }
                            )
                        }
                        composable("profile") {
                            ProfileScreen(
                                onSignOut = {
                                    lifecycleScope.launch {
                                        viewModel.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.popBackStack()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

//    private fun setMainContent(displayWidth: Int, displayHeight: Int, greeting: String) {
//        setContent {
//            LoveGameTheme(dynamicColor = false) {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    ComposeScaffold(displayWidth, displayHeight, greeting){
//                        val signInIntent = googleSignInClient.signInIntent
//                        getResult.launch(signInIntent)
//                    }
//                }
//            }
//        }
//    }

//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithCredential:success")
//                    val user = auth.currentUser
//                    if (user != null) {
//                        user.displayName?.let { setMainContent(displayWidth, displayHeight, it) }
//                    }
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//                }
//            }
//    }

//    companion object {
//        private const val TAG = "GoogleActivity"
//        private const val RC_SIGN_IN = 9001
//    }
}