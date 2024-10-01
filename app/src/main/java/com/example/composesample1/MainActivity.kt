package com.example.composesample1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composesample1.ui.theme.ComposeSample1Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSample1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainCompose(innerPadding)
                }
            }
        }
    }
}

@Composable
fun MainCompose(innerPadding: PaddingValues? = null) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Greeting("Android")
        SimpleButton()
        ShowImage()
        ColumnLayout()
        RowLayout()
        ClickCounter()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        color = Color.White,
        modifier = modifier.background(Color.Blue)
    )
}

@Composable
fun SimpleButton() {
    Button(onClick = {
        /* Handle Click */
    }) {
        Text("Tıklayın")
    }
}

@Composable
fun ShowImage() {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = "Logo"
    )
}

@Composable
fun ColumnLayout() {
    Column {
        Text("Birinci satır")
        Text("İkinci satır")
    }
}

@Composable
fun RowLayout() {
    Row {
        Text("Sol")
        Text("Sağ")
    }
}

@Composable
fun ClickCounter() {
    var count by remember { mutableStateOf(0) }

    Column {
        Text(text = "Butona $count kere tıkladınız.")
        Button(onClick = { count++ }) {
            Text("Tıkla")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeSample1Theme {
        MainCompose()
    }
}