package com.checkforgames.hangman.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.checkforgames.hangman.model.KeyModel
import com.checkforgames.hangman.model.Letter
import com.checkforgames.hangman.ui.theme.Shapes
import com.checkforgames.hangman.utils.Alphabet
import com.checkforgames.hangman.utils.GameStatus
import com.checkforgames.hangman.utils.toKeys
import com.checkforgames.hangman.R
import com.checkforgames.hangman.viewmodel.HangmanViewModel
import java.time.format.TextStyle

@Composable
fun HangmanScreen(hangmanViewModel: HangmanViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Hangman(life = hangmanViewModel.life)
        Word(letters = hangmanViewModel.word)
        Keyboard(
            keys = hangmanViewModel.keyboard,
            onKeyClicked = hangmanViewModel::onKeyClicked,
            gameStatus = hangmanViewModel.gameStatus
        )
    }
    if(hangmanViewModel.gameStatus != GameStatus.Game) {
        GameOver(
            restartGame = hangmanViewModel::restartGame,
            gameStatus = hangmanViewModel.gameStatus
        )
    }
}

@Composable
fun GameOver(
    restartGame: () -> Unit,
    gameStatus: GameStatus
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.8f)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (gameStatus == GameStatus.Win) {
                Text(
                    text = "You Win!",
                    style = MaterialTheme.typography.h4,
                    color = Color.Green
                )
            } else {
                Text(
                    text = "You Lose!",
                    style = MaterialTheme.typography.h4,
                    color = Color.Red
                )
            }
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = restartGame
            ) {
                Text("Play again")
            }
        }
    }

}

@Composable
fun Hangman(life: Int) {
    val configuration = LocalConfiguration.current
    Surface(
        modifier = Modifier
            .size(
                if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                    100.dp
                else
                    200.dp
            )
            .padding(4.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(4.dp)
    ) {
        if(life != 10) {
            Image(
                modifier = Modifier.padding(4.dp),
                painter = painterResource(
                    id = when (life) {
                        9 -> R.drawable.hangman9
                        8 -> R.drawable.hangman8
                        7 -> R.drawable.hangman7
                        6 -> R.drawable.hangman6
                        5 -> R.drawable.hangman5
                        4 -> R.drawable.hangman4
                        3 -> R.drawable.hangman3
                        2 -> R.drawable.hangman2
                        1 -> R.drawable.hangman1
                        else -> R.drawable.hangman1
                    }
                ),
                contentDescription = "hangman"
            )
        }
    }
}

@Composable
fun Keyboard(
    keys: List<KeyModel>,
    gameStatus: GameStatus,
    onKeyClicked: (KeyModel) -> Unit
) {
    val configuration = LocalConfiguration.current
    val maxKeysInRow =
        if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            13
        else
            8
    var keysInRow = maxKeysInRow
    val numOfRemainItems = keys.size % keysInRow
    val numOfRows =
        if(numOfRemainItems == 0)
            keys.size / keysInRow
        else
            keys.size / keysInRow + 1
    val numOfKeysInPenultimateRow = keysInRow - numOfRemainItems
    val numOfKeysInLastRow =  keysInRow + numOfRemainItems - numOfKeysInPenultimateRow
    var index = 0
    Column(
        modifier = Modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(numOfRows) { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
            ) {
                if(numOfRemainItems != 0 && row == numOfRows - 2) {
                    keysInRow = numOfKeysInPenultimateRow
                }
                if(numOfRemainItems != 0 && row == numOfRows - 1) {
                    keysInRow = numOfKeysInLastRow
                }
                repeat(keysInRow) { column ->
                    Key(
                        key = keys[index],
                        onKeyClicked = onKeyClicked,
                        gameStatus = gameStatus
                    )
                    index++
                }
            }
        }
    }
}

@Composable
fun Key(
    modifier: Modifier = Modifier,
    key: KeyModel,
    gameStatus: GameStatus,
    onKeyClicked: (KeyModel) -> Unit
) {
    Surface(
        modifier = modifier
            .size(30.dp)
            .clickable(enabled = (!key.isPressed && gameStatus == GameStatus.Game))
            { onKeyClicked(key) },
        shape = RoundedCornerShape(4.dp),
        color = if(key.isPressed) Color.Gray else MaterialTheme.colors.surface,
        elevation = 4.dp
    ) {
        Text(
            modifier = Modifier.padding(4.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1,
            text = key.letter.uppercase()
        )
    }
}

@Composable
fun Word(letters: List<Letter>) {
    Surface(
        modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp
    ) {
        LazyRow(
            modifier = Modifier.padding(horizontal = 4.dp),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(letters) { letter ->
                Text(
                    text =
                    if (letter.isVisible)
                        letter.letter.uppercase()
                    else
                        "_",
                    style = MaterialTheme.typography.h4
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewKey() {
    Key(key = KeyModel("A"), onKeyClicked = {}, gameStatus = GameStatus.Game)
}

@Preview(showBackground = true)
@Composable
fun PreviewKeyboard() {
    Keyboard(keys = Alphabet().toKeys(), onKeyClicked = {}, gameStatus = GameStatus.Game)
}

@Preview(showBackground = true)
@Composable
fun PreviewWord() {
    Word(
        listOf(
            Letter(letter = "a", isVisible = true),
            Letter(letter = "v", isVisible = false),
            Letter(letter = "t", isVisible = true)
        )
    )
}