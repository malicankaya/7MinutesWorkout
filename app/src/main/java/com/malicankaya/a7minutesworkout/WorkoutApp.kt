package com.malicankaya.a7minutesworkout

import android.app.Application

class WorkoutApp:Application() {
    val db by lazy {
        HistoryDatabase.getInstance(this)
    }
}