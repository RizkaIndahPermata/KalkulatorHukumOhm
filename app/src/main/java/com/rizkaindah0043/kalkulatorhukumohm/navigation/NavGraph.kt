package com.rizkaindah0043.kalkulatorhukumohm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rizkaindah0043.kalkulatorhukumohm.ui.MainScreen
import com.rizkaindah0043.kalkulatorhukumohm.ui.screen.AboutScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen()
        }
        composable(route= Screen.About.route) {
            AboutScreen()
         }
    }
}