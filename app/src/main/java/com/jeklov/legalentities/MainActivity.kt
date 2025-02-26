package com.jeklov.legalentities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jeklov.legalentities.data.db.MainDB
import com.jeklov.legalentities.ui.screens.menu.NavMenuUI
import com.jeklov.legalentities.ui.theme.LegalEntitiesTheme

class MainActivity : ComponentActivity() {
    private lateinit var dataBase: MainDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dataBase = MainDB.getInstance(this)
        setContent {
            LegalEntitiesTheme {
                NavMenuUI(dataBase, this, application)
            }
        }
    }
}