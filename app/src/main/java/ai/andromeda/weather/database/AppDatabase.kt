package ai.andromeda.weather.database

import ai.andromeda.weather.config.Config
import ai.andromeda.weather.models.Weather
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppDatabase(context: Context) {

    private val gson = Gson()
    private val database =
        context.getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE)

    private val currentWeather = MutableLiveData<Weather>()
    private val currentLocation = MutableLiveData<String>()

    suspend fun updateWeather(weather: Weather) {
        withContext(Dispatchers.IO) {
            currentWeather.postValue(weather)
            val data = gson.toJson(weather)
            database.edit().putString(Config.WEATHER_KEY, data).apply()
        }
    }

    fun updateLocation(location: String) {
        currentLocation.postValue(location)
        database.edit().putString(Config.LOCATION_KEY, location).apply()
    }

    fun getWeather(): LiveData<Weather> {
        val data = database.getString(Config.WEATHER_KEY, "")
        currentWeather.value = gson.fromJson(data, Weather::class.java)
        return currentWeather
    }

    fun getLocation(): LiveData<String> {
        val data = database.getString(Config.LOCATION_KEY, null)
        currentLocation.value = data
        return currentLocation
    }
}