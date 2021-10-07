package com.android.vengateshm.composearsenal.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.android.vengateshm.composearsenal.presentation.components.AutoSlider
import com.android.vengateshm.composearsenal.ui.theme.ComposeArsenalTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @ExperimentalUnitApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeArsenalTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }
}

@ExperimentalPagerApi
@ExperimentalUnitApi
@Composable
fun MainScreen() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val drawerItemResult = remember { mutableStateOf("") }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable {
                                if (scaffoldState.drawerState.isClosed)
                                    scope.launch { scaffoldState.drawerState.open() }
                                else
                                    scope.launch { scaffoldState.drawerState.close() }

                            },
                        imageVector = Icons.Default.Menu, contentDescription = null
                    )
                },
                title = { Text(text = "Compose Arsenal") })
        },
        drawerContent = {
            DrawerContent(onDrawerItemClicked = { drawerItem ->
                scope.launch {
                    scaffoldState.drawerState.close()
                }
                drawerItemResult.value = drawerItem
            })
        },
        content = {
            when (drawerItemResult.value) {
                "AutoSlider" -> {
                    AutoSlider()
                }
            }
        })
}

@Composable
fun DrawerContent(onDrawerItemClicked: (String) -> Unit) {
    LazyColumn {
        item {
            DrawerItem(
                itemName = "AutoSlider",
                onDrawerItemClicked = {
                    onDrawerItemClicked(it)
                }
            )
        }
    }
}

@Composable
fun DrawerItem(itemName: String, onDrawerItemClicked: (String) -> Unit) {
    Text(
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                onDrawerItemClicked(itemName)
            }, text = itemName
    )
}