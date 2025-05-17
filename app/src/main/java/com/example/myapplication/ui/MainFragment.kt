package com.example.myapplication.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.interfaces.Callback_HighScoreItemClicked
import com.google.android.gms.location.LocationServices

class MainFragment : AppCompatActivity() {
private lateinit var main_FRAME_highScores: FrameLayout

    private lateinit var main_FRAME_map: FrameLayout

    private lateinit var mapFragment: MapFragment
    private lateinit var highScoresFragment: HighScoresFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_fragment_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViews()
        initViews()
    }

    private fun findViews() {
        main_FRAME_map = findViewById(R.id.main_FRAME_mode)
        main_FRAME_highScores = findViewById(R.id.main_FRAME_highScores)
    }

    private fun initViews() {
        mapFragment = MapFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_mode, mapFragment)
            .commit()

        highScoresFragment = HighScoresFragment()
        highScoresFragment.highScoreItemClicked =
            object :  Callback_HighScoreItemClicked {
                override fun highScoreItemClicked(lat: Double, lon: Double) {
                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainFragment)

                    if (ActivityCompat.checkSelfPermission(
                            this@MainFragment,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@MainFragment,
                            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                            1001
                        )
                        return
                    }

                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            mapFragment.addCurrentLocationMarker(location)
                        }
                    }

                }
            }
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_highScores, highScoresFragment)
            .commit()
    }
}
