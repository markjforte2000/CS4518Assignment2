package wpi.mjforte.cs4518assignment2

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OpenWeatherAPIData {

    inner class OpenWeatherAPIMain {
        @SerializedName("temp")
        @Expose
        public var temp: Double = 0.0
    }

    @SerializedName("main")
    @Expose
    public lateinit var main: OpenWeatherAPIMain

}