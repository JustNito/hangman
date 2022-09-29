package com.checkforgames.hangman

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.checkforgames.hangman.ui.screen.HangmanScreen
import com.checkforgames.hangman.ui.screen.Keyboard
import com.checkforgames.hangman.ui.theme.HangmanTheme
import com.checkforgames.hangman.utils.Alphabet
import com.checkforgames.hangman.utils.toKeys
import com.checkforgames.hangman.viewmodel.HangmanViewModel

class MainActivity : ComponentActivity() {
    
    val hangmanViewModel: HangmanViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainTest", Alphabet().toKeys().toString())
        setContent {
            HangmanTheme {
                HangmanScreen(hangmanViewModel = hangmanViewModel)
            }
        }
    }
}

