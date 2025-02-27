package com.jeklov.legalentities.ui.screens.menu

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jeklov.legalentities.MainActivity
import com.jeklov.legalentities.data.db.MainDB
import com.jeklov.legalentities.ui.screens.Screens
import com.jeklov.legalentities.ui.screens.authorization.LoginUI
import com.jeklov.legalentities.ui.screens.authorization.SignupUI
import com.jeklov.legalentities.ui.screens.chat.ChatPageUI
import com.jeklov.legalentities.ui.screens.chat.ChatUI

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavMenuUI(
    dataBase: MainDB,
    context: MainActivity,
    application: Application
) {

    val navigationController = rememberNavController()

    val screenItemsBar = listOf(
        Screens.Login, Screens.Signup, Screens.Chat, Screens.ChatPage
    )

    val snackbarHostState = remember { SnackbarHostState() }

    // State of topBar, set state to false, if current page showTopBar = false
    val topBarState = rememberSaveable { (mutableStateOf(true)) }

    // State of bottomBar, set state to false, if current page showBottomBar = false
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    val actionButtonState = rememberSaveable { (mutableStateOf(true)) }


    // Some logic for hide bottom menu in some pages (Screens.SomePage.showBottomBar)
    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    screenItemsBar.forEach { screens ->

        // Find real address without transmitting data
        val route = navBackStackEntry?.destination?.route
        val index = route?.indexOf("/")

        val realRoute =
            if (index == -1) route else navBackStackEntry?.destination?.route?.substring(
                0, index!!
            )
        if (realRoute == screens.screen) {
            topBarState.value = screens.showTopBar
            bottomBarState.value = screens.showBottomBar
            actionButtonState.value = screens.showActionButton
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ActionButtonUI(snackbarHostState, dataBase, actionButtonState)
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopBarUI(topBarState)
        },
        bottomBar = {
            BottomBarUI(navigationController, screenItemsBar, bottomBarState)
        }
    ) { paddingValues -> // padding of top bar and bottom bar
        NavHost(
            navController = navigationController, startDestination = Screens.Login.screen
        ) {
            composable(Screens.Login.screen) {
                LoginUI(
                    paddingValues = paddingValues,
                    navigationController = navigationController,
                    context = context,
                    application
                )
            }

            composable(Screens.Signup.screen) {
                SignupUI(
                    paddingValues = paddingValues,
                    navigationController = navigationController,
                    context = context,
                    application
                )
            }

            composable(Screens.Chat.screen) {
                ChatUI(
                    paddingValues = paddingValues,
                    dataBase = dataBase,
                    navigationController = navigationController
                )
            }

            composable(Screens.ChatPage.screen) {
                ChatPageUI(
                    paddingValues = paddingValues,
                    dataBase = dataBase,
                    navigationController = navigationController,
                    snackbarHostState = snackbarHostState,
                    context = context,
                    application = application
                )
            }
        }
    }

}