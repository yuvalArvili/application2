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

## 📂 Project Structure

- `app/src/main/java/com/example/myapplication/` – Main game logic and UI
- `.../utilites/TiltDetector.kt` – Tilt control logic using sensors
- `.../utilites/SingleSoundPlayer.kt` – Handles sound playback
- `res/drawable` – Game graphics (player, rocks, hearts, etc.)
- `res/layout` – XML UI definitions

- Uses:
    - `RecyclerView` for score table
    - `SharedPreferences` for saving the score table
    - **Google Maps API** for location display

## 🛠️ Tech Stack

- **Language**: Kotlin
- **Framework**: Android SDK (compileSdk 35, minSdk 26)
- **Build System**: Gradle (Kotlin DSL)
- **Java Compatibility**: Java 11 (JVM target 11)
- **UI Components**: Material Design (com.google.android.material)
- **Sensors**: Accelerometer via `SensorManager` (for tilt-based control)
- **Sound**: MediaPlayer / SoundPool for sound effects

## 🚀 Getting Started

To build and run the app locally:

1. Clone the repository or download the ZIP.
2. Open the project in **Android Studio** (preferably Electric Eel or newer).
3. Let Gradle sync all dependencies.
4. Connect an Android device or use an emulator (API 26+).
5. Click "Run" (▶) to build and launch the game.

---

