package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.MenuActivity
import com.example.myapplication.R
import com.example.myapplication.interfaces.Callback_HighScoreItemClicked
import com.example.myapplication.logic.ScoreAdapter
import com.example.myapplication.logic.HighScoreManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton

class HighScoresFragment : Fragment() {

    private lateinit var highScores_BTN_send: MaterialButton
    private lateinit var back_menu_BTN: MaterialButton
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var recyclerView: RecyclerView

    private val scoreManager = HighScoreManager()
    private var callback: Callback_HighScoreItemClicked? = null
    fun setCallback(callback: Callback_HighScoreItemClicked) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        val view = inflater.inflate(R.layout.fragment_high_scores, container, false)
        findViews(view)
        initViews()
        setupRecyclerView()
        return view
    }

    private fun findViews(view: android.view.View) {
        highScores_BTN_send = view.findViewById(R.id.highScores_BTN_send)
        back_menu_BTN = view.findViewById(R.id.back_menu_BTN)
        recyclerView = view.findViewById(R.id.leaderboard_recycler)
    }

    private fun initViews() {
        back_menu_BTN.setOnClickListener {
            val intent = Intent(requireContext(), MenuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        val scores = scoreManager.loadHighScores(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        callback?.let {
            recyclerView.adapter = ScoreAdapter(scores, it)
        }
    }
}
