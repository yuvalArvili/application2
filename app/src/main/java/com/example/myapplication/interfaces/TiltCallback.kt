package com.example.myapplication.interfaces

interface TiltCallback {
    fun movePlayerBySensor(x: Float)
    fun adjustRockSpeedByTilt(y: Float)
}
