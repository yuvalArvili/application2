package com.example.myapplication.logic
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import com.example.myapplication.model.Player
import com.example.myapplication.utilites.SignalManager


class GameManager(
    private val maxColumns: Int,
    private val maxRows: Int,
    private val screenMatrix: Array<Array<AppCompatImageView>>,
    private val hearts: Array<AppCompatImageView>,
    private val characters: Array<AppCompatImageView>,
) {
    private val player = Player(column = 1, lives = hearts.size)
    private val rockManager = RockManager(maxColumns, maxRows, screenMatrix) {
        checkCollision()
        updateHeartsUI()
    }

    fun startGame() {
        cleanScreen()
        rockManager.start()

    }
    private fun cleanScreen(){
        characters.forEach { it.visibility = ImageView.INVISIBLE }
        characters[player.column].visibility = View.VISIBLE
        for (i in screenMatrix.indices) {
            for (j in 0 until screenMatrix[i].size) {
                screenMatrix[i][j].visibility = ImageView.INVISIBLE
            }
        }
    }
    fun movePlayerLeft() {
        if (player.column > 0) {
            player.moveLeft()
            updatePlayerPosition()
    }
}

    fun movePlayerRight() {
        if (player.column < maxColumns - 1) {
            player.moveRight(maxColumns)
            updatePlayerPosition()
        }
    }


private fun checkCollision() {
    val row = maxRows - 1
    if (screenMatrix[row][player.column].isVisible) {
        if (player.lives >= 2) {
            SignalManager
                .getInstance()
                .toast("You lost a life!")
            SignalManager
                .getInstance()
                .vibrate()
        }
        player.loseLife()
        if (player.lives <= 0) {
            SignalManager
                .getInstance()
                .toast("Game Over!")
            restartGame()
        }
    }
}


    private fun updateHeartsUI() {
        for (i in hearts.indices) {
            hearts[i].visibility = if (i < player.lives) View.VISIBLE else ImageView.INVISIBLE
        }
    }

    private fun updatePlayerPosition() {
        for (i in characters.indices) {
            characters[i].visibility = if (i == player.column) View.VISIBLE else ImageView.INVISIBLE
        }
    }
    private fun restartGame() {
        rockManager.stop()
        player.lives = player.maxLives
        player.column = maxColumns / 2

        updateHeartsUI()
        cleanScreen()
        SignalManager.
        getInstance().
        toast("Game Restarted!")

        Handler(Looper.getMainLooper()).postDelayed({
            startGame()
        }, 5000)
    }
}

