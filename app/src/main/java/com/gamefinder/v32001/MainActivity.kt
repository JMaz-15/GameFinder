package com.gamefinder.v32001

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gamefinder.v32001.ui.theme.GameFinderTheme
import org.koin.androidx.viewmodel.ext.android.viewModel




class MainActivity : ComponentActivity() {

    private val viewModel : MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchGames()
            val game by viewModel.games.observeAsState(initial = emptyList())
            GameFinderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    FindGame()
                }
                var foo = game
                var i = 1 + 1
            }
        }
    }
}

@Composable
fun FindGame() {
    var gameTitle by remember { mutableStateOf("")}
    val context = LocalContext.current
    Column {
        OutlinedTextField(
            value = gameTitle,
            onValueChange = { gameTitle = it },
            label = { Text(stringResource(R.string.gameTitle)) }
        )
    Button(
        onClick = {
            Toast.makeText(context, "$gameTitle", Toast.LENGTH_LONG).show()
        }){Text(text = "Search")}
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GameFinderTheme {
        FindGame()
    }
}