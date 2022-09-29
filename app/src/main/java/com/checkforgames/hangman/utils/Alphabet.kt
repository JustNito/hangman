package com.checkforgames.hangman.utils

import com.checkforgames.hangman.model.KeyModel

class Alphabet {
    val alphabet = "abcdefghijklmnopqrstuvwxyz"

}
fun Alphabet.toKeys(): List<KeyModel> = alphabet.map {
    KeyModel(letter = it.toString())
}