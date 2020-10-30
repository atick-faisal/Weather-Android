package ai.andromeda.weather.home

import ai.andromeda.weather.*
import ai.andromeda.weather.config.Config
import ai.andromeda.weather.network.Weather
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.home_fragment.view.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.home_fragment, container, false)
        setWeatherChartProperties()

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val application = requireNotNull(this.activity).application

        //----------------- VIEW MODEL SETUP -------------------//
        val viewModelFactory = HomeViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(HomeViewModel::class.java)

        viewModel.location.observe(viewLifecycleOwner, {
            it?.let {
                viewModel.refreshWeather()
            }
        })

        viewModel.weather.observe(viewLifecycleOwner, {
            it?.let {
                viewModel.updateChartData(it)
                updateUI(it)
                // rootView.apiResponseText.text = it.toString()
            }
        })

        viewModel.weatherData.observe(viewLifecycleOwner, {
            it?.let {
                rootView.weatherChart.data = it
                rootView.weatherChart.animateY(1000)
                rootView.weatherChart.invalidate()
                it.notifyDataChanged()
            }
        })
    }

    private fun setWeatherChartProperties() {
        rootView.weatherChart.description.text = ""
        rootView.weatherChart.axisLeft.setDrawLabels(false)
        rootView.weatherChart.axisLeft.isEnabled = false
        rootView.weatherChart.axisRight.setDrawLabels(false)
        rootView.weatherChart.axisRight.isEnabled = false
        rootView.weatherChart.xAxis.setDrawLabels(false)
        rootView.weatherChart.xAxis.setDrawGridLines(false)
        rootView.weatherChart.xAxis.setDrawAxisLine(false)
        rootView.weatherChart.legend.isEnabled = true
        rootView.weatherChart.setTouchEnabled(false)
    }

    private fun updateUI(weather: Weather) {
        try {
            val status = weather.current.status[0]
            val forecastIcon = resources.getIdentifier(
                "ic_${status.icon}",
                "drawable",
                BuildConfig.APPLICATION_ID
            )
            val weatherDescription = status.condition
            val temperature = weather.current.temp
            val feelsLikeTemperature = weather.current.feels_like
            val morningTemperature = weather.hourly[getMorningIndex()].temp
            val afternoonTemperature = weather.hourly[getAfternoonIndex()].temp
            val nightTemperature = weather.hourly[getNightIndex()].temp
            val wind = weather.current.windSpeed
            val humidity = weather.current.humidity
            val visibility = weather.current.visibility
            val uvIndex = weather.current.uvIndex

            Log.i(Config.LOG_TAG, "WIND: $wind")

            rootView.forecastIcon.setImageResource(forecastIcon)
            rootView.weatherDescription.text = weatherDescription
            rootView.temperatureText.text =
                getString(R.string.temperature_text, temperature)
            rootView.feelsLikeText.text =
                getString(R.string.feels_like_temp, feelsLikeTemperature)
            rootView.morningTemperature.text =
                getString(R.string.temperature_text, morningTemperature)
            rootView.afternoonTemperature.text =
                getString(R.string.temperature_text, afternoonTemperature)
            rootView.nightTemperature.text =
                getString(R.string.temperature_text, nightTemperature)

            rootView.windText.text =
                getString(R.string.wind_speed_text, wind)
            rootView.humidityText.text =
                getString(R.string.humidity_text, humidity)
            rootView.visibilityText.text =
                getString(R.string.visibility_text, visibility)
            rootView.uvText.text =
                getString(R.string.uv_index_text, uvIndex)


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}