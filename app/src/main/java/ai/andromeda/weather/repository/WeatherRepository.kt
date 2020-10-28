package ai.andromeda.weather.repository

import ai.andromeda.weather.database.AppDatabase
import ai.andromeda.weather.network.OpenWeatherApi
import ai.andromeda.weather.network.Weather
import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(context: Context) {

    private val database = AppDatabase(context)
    val weather: LiveData<Weather> = database.getWeather()

    suspend fun refreshWeather() {
        withContext(Dispatchers.IO) {
            val weather = OpenWeatherApi.RetrofitService.fetch(
                lat = 23.77,
                lon = 90.42
            )
            weather?.let {
                database.updateWeather(weather)
            }
        }
    }
}