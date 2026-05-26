package com.example.stream.ui.Screen.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun MainScaffold (
    navController: NavController,
    currentRoute: String,
    content: @Composable (PaddingValues) -> Unit
) {
    val navItems = listOf("dashboard", "portal-periksa", "berita", "profil")
    val selectedIndex = navItems.indexOf(currentRoute)

    Scaffold(
        bottomBar = {
            ButtonBar(
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    val target = navItems[index]
                    if (target != currentRoute) {
                        navController.navigate(target) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        },
        content = content
    )
}