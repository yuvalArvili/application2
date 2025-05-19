package com.example.myapplication.logic
//
//import android.content.Context
//import android.util.Log
//import org.json.JSONArray
//
//
//class HighScoreManager {
//    fun saveHighScores(context: Context, scores: List<Int>) {
//        val prefs = context.getSharedPreferences("high_scores", Context.MODE_PRIVATE)
//        val editor = prefs.edit()
//        val json = JSONArray(scores).toString()
//        Log.d("HighScoreManager", "Saving scores: $json") //
//        editor.putString("scores", json)
//        editor.apply()
//    }
//
//    fun loadHighScores(context: Context): List<Int> {
//        val prefs = context.getSharedPreferences("high_scores", Context.MODE_PRIVATE)
//        val json = prefs.getString("scores", null) ?: return emptyList()
//        val jsonArray = JSONArray(json)
//        val list = mutableListOf<Int>()
//        for (i in 0 until jsonArray.length()) {
//            list.add(jsonArray.getInt(i))
//        }
//        return list
//    }
//
//    fun updateHighScores(context: Context, newScore: Int) {
//        Log.d("HighScoreManager", "Saving scores: $newScore") //
//        val currentScores = loadHighScores(context).toMutableList()
//        currentScores.add(newScore)
//        val sorted = currentScores.sortedDescending().take(10)
//        saveHighScores(context, sorted)
//    }
//
//}

import android.content.Context
import android.util.Log
import com.example.myapplication.model.HighScore
import org.json.JSONArray
import org.json.JSONObject

class HighScoreManager {

    fun saveHighScores(context: Context, scores: List<HighScore>) {
        val prefs = context.getSharedPreferences("high_scores", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        val jsonArray = JSONArray()
        for (score in scores) {
            val obj = JSONObject()
            obj.put("score", score.score)
            obj.put("lat", score.latitude)
            obj.put("lon", score.longitude)
            jsonArray.put(obj)
        }

        val jsonString = jsonArray.toString()
        Log.d("HighScoreManager", "Saving scores: $jsonString")
        editor.putString("scores", jsonString)
        editor.apply()
    }

//    fun loadHighScores(context: Context): List<HighScore> {
//        val prefs = context.getSharedPreferences("high_scores", Context.MODE_PRIVATE)
//        val jsonString = prefs.getString("scores", null) ?: return emptyList()
//
//        val jsonArray = JSONArray(jsonString)
//        val list = mutableListOf<HighScore>()
//        for (i in 0 until jsonArray.length()) {
//            val obj = jsonArray.getJSONObject(i)
//            val score = obj.getInt("score")
//            val lat = obj.getDouble("lat")
//            val lon = obj.getDouble("lon")
//            list.add(HighScore(score, lat, lon))
//        }
//        return list
//    }
fun loadHighScores(context: Context): List<HighScore> {
    val prefs = context.getSharedPreferences("high_scores", Context.MODE_PRIVATE)
    val jsonString = prefs.getString("scores", null) ?: return emptyList()

    if (!jsonString.trim().startsWith("[")) {
        Log.e("HighScoreManager", "Invalid JSON array in high scores: $jsonString")
        clearHighScores(context)
        return emptyList()
    }
    val jsonArray = JSONArray(jsonString)
    val highScores = mutableListOf<HighScore>()
    for (i in 0 until jsonArray.length()) {
        val jsonObj = jsonArray.optJSONObject(i)
        if (jsonObj != null) {
            val score = jsonObj.optInt("score", 0)
            val lat = jsonObj.optDouble("lat", 0.0)
            val lon = jsonObj.optDouble("lon", 0.0)
            highScores.add(HighScore(score, lat, lon))
        } else {
            Log.e("HighScoreManager", "Skipped invalid item at index $i: ${jsonArray.get(i)}")
        }
    }
    return highScores
}


    fun updateHighScores(context: Context, newScore: HighScore) {
        val currentScores = loadHighScores(context).toMutableList()
        currentScores.add(newScore)
        val sorted = currentScores.sortedByDescending { it.score }.take(10)
        saveHighScores(context, sorted)
    }
    fun clearHighScores(context: Context) {
        val prefs = context.getSharedPreferences("high_scores", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}
