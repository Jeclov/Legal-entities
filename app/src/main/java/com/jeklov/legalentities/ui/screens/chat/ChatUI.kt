package com.jeklov.legalentities.ui.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jeklov.legalentities.data.db.MainDB
import com.jeklov.legalentities.data.models.User
import com.jeklov.legalentities.data.view.model.UserViewModel
import com.jeklov.legalentities.data.view.model.UserViewModelProviderFactory
import com.jeklov.legalentities.ui.screens.Screens

@OptIn(ExperimentalMaterialApi::class)
@Suppress("PreviewAnnotationInFunctionWithParameters")
@Preview
@Composable
fun ChatUI(
    paddingValues: PaddingValues,
    dataBase: MainDB,
    navigationController: NavHostController
) {

    val viewModel: UserViewModel = viewModel(factory = UserViewModelProviderFactory(dataBase))
    val users = viewModel.allUsers.observeAsState(emptyList())

    //val drawerState = rememberDrawerState(DrawerValue.Closed)

    /*CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalDrawer(
            modifier = Modifier.background(Color.Blue).fillMaxSize(),
            //drawerElevation = 0.dp,
            drawerState = drawerState,
            //gesturesEnabled = true,

            drawerContent = {
                ModalDrawerSheet(
                    Modifier
                        .background(Color.Magenta).fillMaxSize()
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        ChatPageUI()
                    }
                }
            },
        ) {*/
            //CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding()
                        )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LazyColumn(
                            modifier = Modifier.weight(1f)
                        ) {
                            items(users.value) { user ->
                                UserItem(user, navigationController)
                            }
                        }
                    }
                }
            //}
        //}
    //}
}

@Composable
fun UserItem(user: User, navigationController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            userGlobal = user
            navigationController.navigate(Screens.ChatPage.screen)
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(Icons.Default.AccountCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = user.login, fontSize = 20.sp, color = Color.Black)
                Text(text = user.email, fontSize = 16.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun DrawerContent(selectedUser: User?) {
    if (selectedUser != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Детали пользователя", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Логин: ${selectedUser.login}", fontSize = 20.sp)
            Text(text = "Email: ${selectedUser.email}", fontSize = 20.sp)
        }
    } else {
        Text(text = "Пожалуйста, выберите пользователя", fontSize = 20.sp, color = Color.Gray)
    }
}