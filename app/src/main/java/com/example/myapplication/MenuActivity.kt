package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ui.MainFragment

class MenuActivity : AppCompatActivity() {

    private lateinit var switchControlMode: Switch
    private lateinit var labelButtons: TextView
    private lateinit var labelSensors: TextView
    private lateinit var startButton: Button
    private lateinit var recordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_activity)
        findViews()

        startButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USE_SENSORS", switchControlMode.isChecked)
            startActivity(intent)
        }

        recordButton.setOnClickListener {
            val intent = Intent(this, MainFragment::class.java)
            startActivity(intent)
        }
    }
    private fun findViews(){
        switchControlMode = findViewById(R.id.menu_SWITCH_controlMode)
        labelButtons = findViewById(R.id.menu_LBL_buttons)
        labelSensors = findViewById(R.id.menu_LBL_sensors)
        startButton = findViewById(R.id.menu_BTN_start)
        recordButton = findViewById(R.id.record_BTN)
    }

}


