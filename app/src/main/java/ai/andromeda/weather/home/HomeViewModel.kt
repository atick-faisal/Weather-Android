package ai.andromeda.weather.home

import ai.andromeda.weather.repository.LocationRepository
import ai.andromeda.weather.repository.WeatherRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val weatherRepository = WeatherRepository(application)
    private val locationRepository = LocationRepository(application)

    val location = locationRepository.location
    val weather = weatherRepository.weather

    init {
        refreshLocation()
    }

    private fun refreshLocation() {
        viewModelScope.launch {
            locationRepository.refreshLocation()
        }
    }

    fun refreshWeather() {
        viewModelScope.launch {
            weatherRepository.refreshWeather(location.value)
        }
    }
}