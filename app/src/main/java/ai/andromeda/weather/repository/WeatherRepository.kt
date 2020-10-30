package ai.andromeda.weather.repository

import ai.andromeda.weather.config.Config
import ai.andromeda.weather.database.AppDatabase
import ai.andromeda.weather.network.OpenWeatherApi
import ai.andromeda.weather.models.Weather
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(context: Context) {

    private val database = AppDatabase(context)
    val weather: LiveData<Weather> = database.getWeather()

    suspend fun refreshWeather(location: String?) {
        withContext(Dispatchers.IO) {
            try {
                val latLong = location!!.split(",")
                val weather = OpenWeatherApi.RetrofitService.fetch(
                    lat = latLong[0].toDouble(),
                    lon = latLong[1].toDouble()
                )
                weather?.let {
                    Log.i(Config.LOG_TAG, "NETWORK: ${weather.daily.size}")
                    database.updateWeather(weather)
                }
            } catch (e: Exception) {
                Log.i(Config.LOG_TAG, "WEATHER REPO: BAD REQUEST")
            }
        }
    }
}