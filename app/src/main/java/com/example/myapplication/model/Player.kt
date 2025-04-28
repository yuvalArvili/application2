package com.example.myapplication.model

class Player(
    var column: Int,
    var lives: Int,
    val maxLives: Int = 3

) {
    fun moveLeft() {
        if (column > 0) column--
    }

    fun moveRight(maxColumns: Int) {
        if (column < maxColumns - 1) column++
    }

    fun loseLife() {
        lives--
    }
}