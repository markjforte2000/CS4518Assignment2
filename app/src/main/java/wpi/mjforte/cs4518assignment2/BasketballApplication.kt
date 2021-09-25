package wpi.mjforte.cs4518assignment2

import android.app.Application

class BasketballApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        BasketballRepository.initialize(this)
    }

}