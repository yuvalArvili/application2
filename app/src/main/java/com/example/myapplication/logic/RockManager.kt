package com.example.myapplication.logic
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

class RockManager(
    private val maxColumns: Int,
    private val maxRows: Int,
    private val rocks: Array<Array<Boolean>> = Array(maxRows) { Array(maxColumns) { false } },
    private val playerCol: Int,
    private val screenMatrix: Array<Array<AppCompatImageView>>,
    private val onRockMoved: () -> Unit // callback for collision check
) {
    private val moveHandler = Handler(Looper.getMainLooper())
    private val spawnHandler = Handler(Looper.getMainLooper())
    private var isRunning = false

    private val moveRunnable = object : Runnable {
        override fun run() {
            if (!isRunning) return
            moveRocksDown()
            moveHandler.postDelayed({
                onRockMoved()
            }, 200)
            moveHandler.postDelayed(this, 1000) //every sec rock move down
        }
    }

    private val spawnRunnable = object : Runnable {
        override fun run() {
            if (!isRunning) return
            spawnNewRock()
            spawnHandler.postDelayed(this, 2000) //every 2 sec appear new rock
        }
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
            val lastRowRock = screenMatrix[numRows - 1][col]
            if (lastRowRock.isVisible) {
                lastRowRock.visibility = View.INVISIBLE
            }
        }

        for (row in numRows - 2 downTo 0) {
            for (col in 0 until numCols) {
                val currentRock = screenMatrix[row][col]
                val belowRock = screenMatrix[row + 1][col]

                if (currentRock.isVisible && belowRock.isInvisible) {
                    currentRock.visibility = View.INVISIBLE
                    belowRock.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun spawnNewRock() {
        val col = (0 until maxColumns).random()
        if (screenMatrix[0][col].isInvisible) {
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