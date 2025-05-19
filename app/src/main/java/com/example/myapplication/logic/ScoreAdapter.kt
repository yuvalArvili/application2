package com.example.myapplication.logic

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.interfaces.Callback_HighScoreItemClicked
import com.example.myapplication.model.HighScore

class ScoreAdapter(
    private val scores: List<HighScore>,
    private val callback: Callback_HighScoreItemClicked
) : RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeText: TextView = itemView.findViewById(R.id.score_place)
        val scoreText: TextView = itemView.findViewById(R.id.score_points)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val highScore = scores[position]
        holder.placeText.text = (position + 1).toString()
        holder.scoreText.text = "  Score: ${highScore.score}"

        holder.itemView.setOnClickListener {
            callback.highScoreItemClicked(highScore.latitude, highScore.longitude)
            Log.d("HighScoresFragment", "Clicked location: ${highScore.latitude}, ${highScore.longitude}")

        }
    }

    override fun getItemCount(): Int = scores.size
}

