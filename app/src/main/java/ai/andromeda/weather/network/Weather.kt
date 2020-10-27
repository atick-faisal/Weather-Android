package ai.andromeda.weather.network

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("main")
    val current: Current
)

data class Current(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Double,
    val humidity: Double
)

//"temp":305.15,
//"feels_like":307.46,
//"temp_min":305.15,
//"temp_max":305.15,
//"pressure":1008,
//"humidity":52