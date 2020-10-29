package ai.andromeda.weather.home

import ai.andromeda.weather.R
import ai.andromeda.weather.config.Config
import ai.andromeda.weather.network.Weather
import ai.andromeda.weather.repository.LocationRepository
import ai.andromeda.weather.repository.WeatherRepository
import android.app.Application
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val chartColor = ContextCompat.getColor(
        application, R.color.colorPrimary
    )
    private val chartBackground = ContextCompat.getDrawable(
        application, R.drawable.chart_gradient
    )

    private val weatherRepository = WeatherRepository(application)
    private val locationRepository = LocationRepository(application)

    val location = locationRepository.location
    val weather = weatherRepository.weather

    private val _weatherData = MutableLiveData<LineData>()
    val weatherData: LiveData<LineData>
        get() = _weatherData

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

    fun updateChartData(weather: Weather) {
        val tempValues = mutableListOf<Double>()
        try {
            val hourly = weather.hourly
            for (hour in hourly) {
                tempValues.add(hour.temp)
            }
            Log.i(Config.LOG_TAG, "HOME_VM: HOURLY DATA LENGTH ${tempValues}")
            val tempItems: MutableList<Entry> = mutableListOf()
            tempValues.forEachIndexed { index, value ->
                tempItems.add(Entry(index.toFloat(), value.toFloat()))
            }
            val tempDataSet = LineDataSet(
                tempItems,
                "High ${tempValues.maxOrNull()} °C Low ${tempValues.minOrNull()} °C"
            )
            tempDataSet.color = chartColor
            tempDataSet.fillDrawable = chartBackground
            tempDataSet.setDrawValues(false)
            tempDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
            tempDataSet.setDrawFilled(true)
            tempDataSet.setDrawCircleHole(false)
            tempDataSet.setDrawCircles(false)
            tempDataSet.lineWidth = 3.0f
            _weatherData.value = LineData(tempDataSet)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}