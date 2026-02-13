package com.example.examen08_02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.examen08_02.home.HomeScreen
import com.example.examen08_02.home.HomeViewModel
import com.example.examen08_02.login.LoginScreen
import com.example.examen08_02.login.LoginViewModel
import com.example.examen08_02.network.GhibliApiService
import com.example.examen08_02.network.GhibliFilms
import com.example.examen08_02.ui.theme.Examen0802Theme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var auth = FirebaseAuth.getInstance()
        val apiService = GhibliFilms.retrofitService
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            Examen0802Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(LoginViewModel(), auth) {
                                navController.navigate("home")
                            }
                        }
                        composable("home") {
                            HomeScreen(HomeViewModel(apiService), auth )
                        }
                    }
                }
            }
        }
    }
}