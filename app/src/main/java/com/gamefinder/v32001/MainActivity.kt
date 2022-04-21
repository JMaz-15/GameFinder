package com.gamefinder.v32001

import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.gamefinder.dto.Game
import com.gamefinder.dto.GameInfo
import com.gamefinder.v32001.ui.theme.GameFinderTheme
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {

    private var selectedGameTitle: GameInfo? = null
    private var selectedGame: Game? = null
    private val viewModel: MainViewModel by viewModel<MainViewModel>()
    private var strSelectedData: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchGames()
            val games by viewModel.games.observeAsState(initial = emptyList())
            val gameList by viewModel.localGame.observeAsState(initial = emptyList())

            
            GameFinderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    FindGame("Android", games, gameList)
                }
                var foo = games
                val i = 1+1
            }
        }
    }
    @Composable
    fun gameDropDown(localGames: List<GameInfo>){
        var gameText by remember {mutableStateOf("")}
        var expanded by remember { mutableStateOf(false)}
        Box(Modifier.fillMaxWidth()){
            Row(
                Modifier
                    .padding(20.dp)
                    .clickable {
                        expanded = !expanded
                    }
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = gameText, fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}){
                localGames.forEach {
                        game -> DropdownMenuItem(onClick = {
                        expanded = false
                        gameText = ("Title: " + game.title + "\r\n" + "URL: " +  game.steamUrl + "\r\n" + "Status: " +  game.status)
                        selectedGameTitle = game
                    }) {
                        Text(text = game.title)
                    }

                }
                }
            }
        }
    }

    @Composable
    fun TextFieldWithDropdownUsage(dataIn: List<Game>, label: String = "") {

        val dropDownOptions = remember { mutableStateOf(listOf<Game>()) }
        val textFieldValue = remember { mutableStateOf(TextFieldValue()) }
        val dropDownExpanded = remember { mutableStateOf(false) }
        fun onDropdownDismissRequest() {
            dropDownExpanded.value = false
        }

        fun onValueChanged(value: TextFieldValue) {
            strSelectedData = value.text
            dropDownExpanded.value = true
            textFieldValue.value = value
            dropDownOptions.value = dataIn.filter {
                it.toString().startsWith(value.text, true) && it.toString() != value.text
            }.take(3)
        }


        TextFieldWithDropdown(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue.value,
            setValue = ::onValueChanged,
            onDismissRequest = ::onDropdownDismissRequest,
            dropDownExpanded = dropDownExpanded.value,
            list = dropDownOptions.value,
            label = label
        )
    }

        @Composable
        fun TextFieldWithDropdown(
            modifier: Modifier = Modifier,
            value: TextFieldValue,
            setValue: (TextFieldValue) -> Unit,
            onDismissRequest: () -> Unit,
            dropDownExpanded: Boolean,
            list: List<Game>,
            label: String = ""
        ) {
            Box(modifier) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused)
                                onDismissRequest()
                        },
                    value = value,
                    onValueChange = setValue,
                    label = { Text(label) },
                    colors = TextFieldDefaults.outlinedTextFieldColors()
                )
                DropdownMenu(
                    expanded = dropDownExpanded,
                    properties = PopupProperties(
                        focusable = false,
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true
                    ),
                    onDismissRequest = onDismissRequest
                ) {
                    list.forEach { text ->
                        DropdownMenuItem(onClick = {
                            setValue(
                                TextFieldValue(
                                    text.toString(),
                                    TextRange(text.toString().length)
                                )
                            )
                            selectedGame = text
                        }) {
                            Text(text = text.toString())
                        }
                    }
                }
            }
        }
        @Composable
        fun FindGame(name: String, games: List<Game> = ArrayList<Game>(), game: List<GameInfo> = ArrayList<GameInfo>()) {
            var gameTitle by remember { mutableStateOf("") }


            val context = LocalContext.current
            Column( horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(30.dp)) {
                TextFieldWithDropdownUsage(dataIn = games, stringResource(R.string.game_Title))
                Button(
                    onClick = {
                       var gameInfo = GameInfo().apply {
                            gameId = selectedGame?.let {
                                it.gameId

                            } ?: 0
                            title = selectedGame?.let {
                                it.title
                            } ?: ""
                            steamUrl = selectedGame?.let {
                                it.steamUrl
                            } ?: ""
                            status = selectedGame?.let {
                                it.status
                            } ?: ""
                        }

                        println(gameInfo.gameId.toString()+"")

                        for (i in 0..2) {

                            val toast = Toast.makeText(
                                context,
                                "ID:    " + gameInfo.gameId.toString() + "\r\n\r\nTitle:    " +
                                        gameInfo.title + "\r\n\r\nURL:    " + gameInfo.steamUrl +
                                        "\r\n\r\nStatus:    " + gameInfo.status, Toast.LENGTH_LONG
                            )
                            toast.setGravity(Gravity.TOP, 0, 1000)

                            toast.show()
                        }          

                            DisplayGameInfo()
                        Toast.makeText(context, "$gameTitle", Toast.LENGTH_LONG).show()
                    }) { Text(text = "Search") }
                Button(
                    onClick = {
                        var gameInfo = GameInfo().apply {
                            gameId = selectedGame?.let {
                                it.gameId
                            } ?: 0
                            title = selectedGame?.let {
                                it.title
                            } ?: ""
                            steamUrl = selectedGame?.let {
                                it.steamUrl
                            } ?: ""
                            status = selectedGame?.let {
                                it.status
                            } ?: ""
                        }
                        viewModel.save(gameInfo)
                    }) { Text(text = "Save Game")}
                gameDropDown(localGames = game)
            }
        }


    fun DisplayGameInfo(){
        var gameInfo = GameInfo().apply {
            gameId = selectedGame?.let {
                it.gameId
            } ?: 0
            title = selectedGame?.let {
                it.title
            } ?: ""
            steamUrl = selectedGame?.let {
                it.steamUrl
            } ?: ""
            status = selectedGame?.let {
                it.status
            } ?: ""
        }

    }
        @Preview(showBackground = true)
        @Composable
        fun DefaultPreview() {
            GameFinderTheme {
                FindGame("Android")
            }
        }


}
