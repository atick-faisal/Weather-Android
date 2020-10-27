package ai.andromeda.weather.network

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("name")
    val cityName: String,

    @SerializedName("weather")
    val status: List<Status>,

    @SerializedName("main")
    val current: Current,

    val visibility: Long,
    val wind: Wind,

    @SerializedName("sys")
    val sun: Sun
)

data class Status(
    val id: Long,

    @SerializedName("main")
    val condition: String,

    val description: String,
    val icon: String
)

data class Current(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Double,
    val humidity: Double
)

data class Wind(
    val speed: Double,
    val deg: Double
)

data class Sun(
    val sunrise: Long,
    val sunset: Long
)