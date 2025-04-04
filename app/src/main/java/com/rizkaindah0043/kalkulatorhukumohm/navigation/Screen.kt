package com.rizkaindah0043.kalkulatorhukumohm.navigation

sealed class Screen (val route: String) {
    data object Home: Screen("mainScreen")
    data object About: Screen("aboutScreen")
}