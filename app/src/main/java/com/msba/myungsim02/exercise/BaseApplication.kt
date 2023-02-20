package com.msba.myungsim02.exercise

import android.app.Application
import com.msba.myungsim02.exercise.data.HealthConnectManager

class BaseApplication : Application() {
    val healthConnectManager by lazy {
        HealthConnectManager(this)
    }
}
