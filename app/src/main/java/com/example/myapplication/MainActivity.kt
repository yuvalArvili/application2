package com.example.myapplication
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.example.myapplication.logic.GameManager
import com.example.myapplication.logic.HighScoreManager
import com.example.myapplication.ui.MainFragment
import com.example.myapplication.utilites.SignalManager


class MainActivity : AppCompatActivity() {
    private lateinit var main_IMG_hearts: Array<AppCompatImageView>
    private lateinit var main_IMG_charcters: Array<AppCompatImageView>
    private lateinit var main_IMG_rocks: Array<Array<AppCompatImageView>>
    private lateinit var scoreCounter: TextView
    private lateinit var btnLeft: Button
    private lateinit var btnRight: Button
    private lateinit var layout: RelativeLayout
    private lateinit var gameManager: GameManager
    private val maxColumns = 5
    private val maxRows = 8
    private var useSensors: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useSensors = intent.getBooleanExtra("USE_SENSORS", false)
        setContentView(R.layout.activity_main)
        SignalManager.init(this)
        findViews()

        gameManager = GameManager(this,
            maxColumns,
            maxRows,
            main_IMG_rocks,
            main_IMG_hearts,
            main_IMG_charcters,
            scoreCounter,
            useSensors
        )

        gameManager.onGameOver = {
            runOnUiThread {
                val intent = Intent(this, MainFragment::class.java)
                startActivity(intent)
                finish()
            }
        }
        game()

    }

    private fun findViews(){
        btnLeft = findViewById(R.id.main_BTN_left)
        btnRight = findViewById(R.id.main_BTN_right)
        layout = findViewById(R.id.main)
        scoreCounter = findViewById(R.id.scoreCounter)

        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )
        main_IMG_charcters = arrayOf(
            findViewById(R.id.main_IMG_character0),
            findViewById(R.id.main_IMG_character1),
            findViewById(R.id.main_IMG_character2),
            findViewById(R.id.main_IMG_character3),
            findViewById(R.id.main_IMG_character4)
        )
        main_IMG_rocks = arrayOf(
           arrayOf( findViewById(R.id.cell_0_0),
           findViewById(R.id.cell_0_1),
           findViewById(R.id.cell_0_2),
           findViewById(R.id.cell_0_3),
           findViewById(R.id.cell_0_4)),

           arrayOf( findViewById(R.id.cell_1_0),
            findViewById(R.id.cell_1_1),
            findViewById(R.id.cell_1_2),
            findViewById(R.id.cell_1_3),
            findViewById(R.id.cell_1_4)),

            arrayOf(findViewById(R.id.cell_2_0),
            findViewById(R.id.cell_2_1),
            findViewById(R.id.cell_2_2),
            findViewById(R.id.cell_2_3),
            findViewById(R.id.cell_2_4)),

            arrayOf(findViewById(R.id.cell_3_0),
            findViewById(R.id.cell_3_1),
            findViewById(R.id.cell_3_2),
            findViewById(R.id.cell_3_3),
            findViewById(R.id.cell_3_4)),

            arrayOf(findViewById(R.id.cell_4_0),
            findViewById(R.id.cell_4_1),
            findViewById(R.id.cell_4_2),
            findViewById(R.id.cell_4_3),
            findViewById(R.id.cell_4_4)),

            arrayOf(findViewById(R.id.cell_5_0),
            findViewById(R.id.cell_5_1),
            findViewById(R.id.cell_5_2),
            findViewById(R.id.cell_5_3),
            findViewById(R.id.cell_5_4)),

            arrayOf(findViewById(R.id.cell_6_0),
            findViewById(R.id.cell_6_1),
            findViewById(R.id.cell_6_2),
            findViewById(R.id.cell_6_3),
            findViewById(R.id.cell_6_4)),

            arrayOf(findViewById(R.id.cell_7_0),
            findViewById(R.id.cell_7_1),
            findViewById(R.id.cell_7_2),
            findViewById(R.id.cell_7_3),
            findViewById(R.id.cell_7_4))
        )
    }
    private fun hideControlButtons() {
        if (useSensors){
           btnLeft.visibility = View.INVISIBLE
            btnRight.visibility = View.INVISIBLE
        }
    }
    private fun game() {
        hideControlButtons()
        gameManager.startGame()

        btnLeft.setOnClickListener {
            gameManager.movePlayerLeft()
        }

        btnRight.setOnClickListener {
            gameManager.movePlayerRight()
        }
    }

    override fun onPause() {
        super.onPause()
        gameManager.stopGame()
    }

}








