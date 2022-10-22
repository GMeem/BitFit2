package com.example.sleepbit2

import android.app.Application

class SleepApplication : Application() {
    val db by lazy { AppDatabase.getInstance(this) }
}