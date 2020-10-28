package ai.andromeda.weather.home

import ai.andromeda.weather.config.Config
import ai.andromeda.weather.repository.WeatherRepository
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val weatherRepository = WeatherRepository(application)

    val weather = weatherRepository.weather

    init {
        refreshWeather()
    }

    private fun refreshWeather() {
        viewModelScope.launch {
            try {
               weatherRepository.refreshWeather()
            } catch(e: Exception) {
                e.printStackTrace()
                Log.i(Config.LOG_TAG, "HOME: NETWORK ERROR")
            }
        }
    }
}