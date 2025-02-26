package com.jeklov.legalentities.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import com.jeklov.legalentities.R

sealed class Screens(
    val titleRes: Int,
    val screen: String,
    @DrawableRes val iconRes: Int? = null,
    val showIconOnBottomBar: Boolean = true,
    val showTopBar: Boolean = true,
    val showBottomBar: Boolean = true,
    val showActionButton: Boolean = false
) {
    data object Signup : Screens(
        titleRes = R.string.signup,
        screen = "signup",
        showTopBar = false,
        showBottomBar = false,
        showIconOnBottomBar = false
    )
    data object Login : Screens(
        titleRes = R.string.login,
        screen = "login",
        showTopBar = false,
        showBottomBar = false,
        showIconOnBottomBar = false
    )
    data object Chat : Screens(
        titleRes = R.string.chat,
        screen = "chat",
        iconRes = R.drawable.chat,
        showTopBar = false,
        showActionButton = true
    )

    data object ChatPage : Screens(
        titleRes = R.string.chat,
        screen = "chat_page",
        showTopBar = false,
        showBottomBar = false,
        showIconOnBottomBar = false
    )
}