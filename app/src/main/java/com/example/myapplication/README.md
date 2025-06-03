# Falling Rocks Game – Android App

## 🕹️ Overview
Falling Rocks is an Android mobile game where the player controls a character positioned at the bottom of the screen.  
The objective is to **avoid falling rocks**, **collect bonuses**, and **survive as long as possible**.  
Players can choose between **tilt-based sensor control** and **button-based control**.

## ⚙️ Main Features

- **Control Options**:
    - **Sensor Mode**:
        - Tilt **right** to move right
        - Tilt **left** to move left
        - Tilt **forward** to **speed up**
        - Tilt **backward** to **slow down**
    - **Button Mode**:
        - On-screen **left** and **right** buttons for movement

- **Gameplay Elements**:
    - **Five-lane road** for rich movement variety
    - **Crash sound effect** when hitting rocks
    - **Collect coins** to increase score (with sound effect)
    - **Collect hearts** to regain lives (max 3 lives, with sound effect)
    - **Odometer** tracks distance traveled – affects final score
    - **High score saving**, including the **location** of each record

## 🎮 Main Menu
- Choose from two game modes:
    - **Buttons for right and left**
    - **Sensor-based controls**
- Navigate to the **High Scores** screen

## 🏆 High Scores Screen

- Built using **two Fragments**:
    1. **Score Table** – top 10 scores since installation
    2. **Map View** – shows where each high score occurred
- Selecting a score in the table updates the map accordingly

## 📦 Project Structure

- Modular codebase:
    - `GameManager` – handles main game logic
    - `RockManager` – generates and manages obstacles
    - `HighScoreManager` – tracks scores and locations
    -  `TiltDetector` – manages all the sensors' work
  
    - Additional helper classes handle animations, sound effects, UI elements, and data persistence.

- Uses:
    - `RecyclerView` for score table
    - **Google Maps API** for location display
---

