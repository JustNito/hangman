package com.checkforgames.hangman.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.checkforgames.hangman.model.KeyModel
import com.checkforgames.hangman.model.Letter
import com.checkforgames.hangman.utils.Alphabet
import com.checkforgames.hangman.utils.GameStatus
import com.checkforgames.hangman.utils.toKeys
import com.checkforgames.hangman.utils.words
import kotlinx.coroutines.launch

class HangmanViewModel : ViewModel() {

    private val _word = mutableStateListOf<Letter>()
    val word
        get() = _word

    private var _gameStatus by mutableStateOf(GameStatus.Game)
    val gameStatus
        get() = _gameStatus

    private var _life by mutableStateOf(10)
    val life
        get() = _life

    private val _keyboard = mutableStateListOf<KeyModel>()
    val keyboard
        get() = _keyboard

    init {
        _word.addAll(getRandomWord())
        _keyboard.addAll(Alphabet().toKeys())
    }

    fun restartGame() {
        _life = 10
        _gameStatus = GameStatus.Game
        _keyboard.clear()
        _keyboard.addAll(Alphabet().toKeys())
        _word.clear()
        _word.addAll(getRandomWord())
    }

    fun onKeyClicked(key: KeyModel) {
        var isLetterFound = false
        for (index in 0 until _word.size) {
            if (key.letter == _word[index].letter) {
                isLetterFound = true
                _word[index] = _word[index].copy(isVisible = true)
            }
        }
        if(isLetterFound) {
            correct()
        } else {
            misstake()
        }
        val indexOfKey = _keyboard.indexOf(_keyboard.find { it.letter == key.letter })
        _keyboard[indexOfKey] = _keyboard[indexOfKey].copy(isPressed = true)
    }

    private fun misstake() {
        _life--
        if(life == 1) {
            loss()
        }
    }

    private fun correct() {
        if(_word.all { it.isVisible }) {
            win()
        }
    }

    private fun loss() {
        _gameStatus = GameStatus.Loss
    }

    private fun win() {
        _gameStatus = GameStatus.Win
    }

    private fun getRandomWord(): List<Letter> = wordToLetterList(words.random())

    private fun wordToLetterList(word: String): List<Letter> = word.map {
        Letter(letter = it.toString())
    }
}