package com.example.myapplication.logic
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.myapplication.R
import com.example.myapplication.model.Player
import com.example.myapplication.utilites.SignalManager
import com.example.myapplication.utilites.SingleSoundPlayer
import com.example.myapplication.interfaces.TiltCallback
import com.example.myapplication.model.HighScore
import com.example.myapplication.utilites.TiltDetector
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource

class GameManager(
    private val context: Context,
    private val maxColumns: Int,
    private val maxRows: Int,
    private val screenMatrix: Array<Array<AppCompatImageView>>,
    private val hearts: Array<AppCompatImageView>,
    private val characters: Array<AppCompatImageView>,
    private val scoreCounter: TextView,
    private val useSensors: Boolean,
) : TiltCallback {

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var tiltDetector: TiltDetector? = null
    private val player = Player(column = 2, lives = hearts.size)
    private var meters = 0
    private var scoreManager = HighScoreManager()
    var onGameOver: (() -> Unit)? = null

    private val rockManager = RockManager(maxColumns, maxRows,screenMatrix,hearts) {
        checkCollision()
        updateHeartsUI()
        meters++
        spawnHeartIfNeeded()
        spawnCoins()
        updateMetersUI()
        checkGameOver()
    }

    fun startGame() {
        if (useSensors) {
            tiltDetector = TiltDetector(context, this)
            tiltDetector?.start()
        }
        cleanScreen()
        rockManager.start()

    }
    private fun cleanScreen(){
        characters.forEach { it.visibility = ImageView.INVISIBLE }
        characters[player.column].visibility = View.VISIBLE
        rockManager.clearAllRocks()
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
    override fun movePlayerBySensor(x: Float) {
        if (x >= 3.0) {
            movePlayerLeft()
        } else if (x <= -3.0) {
            movePlayerRight()
        }
    }

    override fun adjustRockSpeedByTilt(y: Float) {
        val maxTilt = 5.0f
        val minDelay = 1500L
        val maxDelay = 500L

        val clampedTilt = y.coerceIn(0f, maxTilt)
        val delay = ((1 - (clampedTilt / maxTilt)) * (maxDelay - minDelay) + minDelay).toLong()

        rockManager?.setFallDelay(delay)
    }

    private fun updateMetersUI() {
        scoreCounter.text = "$meters"
    }

    private fun spawnHeartIfNeeded() {
        if (player.lives >= 3) {
            clearAllHeartsOnScreen()
            return
        }

        val currentLives = player.lives
        val shouldSpawn = (0..4).random() == 0
        if (currentLives < 3 && shouldSpawn) {
            val col = (0 until maxColumns).random()
            if (screenMatrix[0][col].isInvisible) {
                screenMatrix[0][col].scaleType = ImageView.ScaleType.FIT_CENTER
                screenMatrix[0][col].setImageResource(R.drawable.add_heart)
                screenMatrix[0][col].tag = "HEART"
                screenMatrix[0][col].visibility = View.VISIBLE
            }
        }
    }

    private fun spawnCoins() {
        val shouldSpawn = (0..8).random() == 0
        if (shouldSpawn) {
            val col = (0 until maxColumns).random()
            if (screenMatrix[0][col].isInvisible) {
                screenMatrix[0][col].scaleType = ImageView.ScaleType.FIT_CENTER
                screenMatrix[0][col].setImageResource(R.drawable.coin)
                screenMatrix[0][col].tag = "COIN"
                screenMatrix[0][col].visibility = View.VISIBLE
            }
        }
    }

    private fun clearAllHeartsOnScreen() {
        for (row in 0 until maxRows) {
            for (col in 0 until maxColumns) {
                val view = screenMatrix[row][col]
                if (view.tag == "HEART") {
                    view.visibility = View.INVISIBLE
                    view.tag = null
                }
            }
        }
    }

    private fun checkCollision() {
        val row = maxRows - 1
        val currentView = screenMatrix[row][player.column]

        if (currentView.isVisible) {
            when (currentView.tag) {
                "ROCK" -> {
                    if (player.lives >= 2) {
                        SignalManager.getInstance().toast("You lost a life!")
                        SignalManager.getInstance().vibrate()
                        SingleSoundPlayer(context).playSound(R.raw.boom)
                    }
                    player.loseLife()
                }
                "HEART" -> {
                    if (player.lives < 3) {
                        player.gainLife()
                        SignalManager.getInstance().toast("You got a heart!")
                        SingleSoundPlayer(context).playSound(R.raw.prize)

                    }
                }
                "COIN" -> {
                    meters += 30
                    SignalManager.getInstance().toast("You got a prize!")
                    SingleSoundPlayer(context).playSound(R.raw.prize)
                }
            }
            currentView.visibility = View.INVISIBLE
            currentView.tag = null
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

    private fun checkGameOver() {
        if (player.lives <= 0) {
            SignalManager.getInstance().toast("Game Over!")
            stopGame()
        }
    }

     fun stopGame() {
         getCurrentLocationAndSaveHighScore()
         tiltDetector?.stop()
         rockManager.stop()
         onGameOver?.invoke()


    }

    private fun getCurrentLocationAndSaveHighScore() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            saveHighScoreWithoutLocation()
            onGameOver?.invoke()
            return
        }

        fusedLocationClient.getCurrentLocation(
            com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener { location ->
            val lat = location?.latitude ?: 0.0
            val lon = location?.longitude ?: 0.0
            val newHighScore = HighScore(meters, lat, lon)
            scoreManager.updateHighScores(context, newHighScore)
            onGameOver?.invoke()
        }.addOnFailureListener {
            saveHighScoreWithoutLocation()
            onGameOver?.invoke()
        }
    }


    private fun saveHighScoreWithoutLocation() {
        val newHighScore = HighScore(meters, 0.0, 0.0)
        scoreManager.updateHighScores(context, newHighScore)
    }
}

