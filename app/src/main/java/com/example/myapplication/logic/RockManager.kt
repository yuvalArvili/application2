package com.example.myapplication.logic
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.myapplication.R

class RockManager(
    private val maxColumns: Int,
    private val maxRows: Int,
    private val screenMatrix: Array<Array<AppCompatImageView>>,
    private val hearts: Array<AppCompatImageView>,
    private val onRockMoved: () -> Unit // callback for collision check
) {
    private val moveHandler = Handler(Looper.getMainLooper())
    private val spawnHandler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private var fallDelay: Long = 1000 //

    private val moveRunnable = object : Runnable {
        override fun run() {
            if (!isRunning) return
            moveRocksDown()
            moveHandler.postDelayed({
                onRockMoved()
            }, 200)
            moveHandler.postDelayed(this, fallDelay) //every sec rock move down
        }
    }

    private val spawnRunnable = object : Runnable {
        override fun run() {
            if (!isRunning) return
            spawnNewRock()
            spawnHandler.postDelayed(this, 2000) //every 2 sec appear new rock
        }
    }

    fun setFallDelay(newDelay: Long) {
        fallDelay = newDelay
    }

    fun start() {
        if (isRunning) return
        isRunning = true
        moveHandler.post(moveRunnable)
        spawnHandler.post(spawnRunnable)
    }


fun moveRocksDown() {
    val numRows = maxRows
    val numCols = maxColumns

    for (col in 0 until numCols) {
        val lastRowView = screenMatrix[numRows - 1][col]
        if (lastRowView.isVisible) {
            lastRowView.visibility = View.INVISIBLE
            lastRowView.tag = null
        }
    }

    for (row in numRows - 2 downTo 0) {
        for (col in 0 until numCols) {
            val currentView = screenMatrix[row][col]
            val belowView = screenMatrix[row + 1][col]

            if (currentView.isVisible && belowView.isInvisible) {
                belowView.setImageDrawable(currentView.drawable)
                belowView.tag = currentView.tag
                belowView.visibility = View.VISIBLE

                currentView.visibility = View.INVISIBLE
                currentView.tag = null
            }
        }
    }
}

    private fun spawnNewRock() {
        val col = (0 until maxColumns).random()
        if (screenMatrix[0][col].isInvisible) {
            screenMatrix[0][col].setImageResource(R.drawable.rock)
            screenMatrix[0][col].tag = "ROCK"
            screenMatrix[0][col].visibility = View.VISIBLE
        }
    }


    fun clearAllRocks() {
        for (row in screenMatrix.indices) {
            for (col in screenMatrix[row].indices) {
                screenMatrix[row][col].visibility = View.INVISIBLE
            }
        }
    }
    fun stop() {
        isRunning = false
        moveHandler.removeCallbacks(moveRunnable)
        spawnHandler.removeCallbacks(spawnRunnable)
    }
}