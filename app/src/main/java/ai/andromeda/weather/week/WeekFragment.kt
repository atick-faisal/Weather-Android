package ai.andromeda.weather.week

import ai.andromeda.weather.BuildConfig
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ai.andromeda.weather.R
import ai.andromeda.weather.config.Config
import ai.andromeda.weather.getCurrentDayOfWeek
import ai.andromeda.weather.getDayOfWeek
import ai.andromeda.weather.shared.WeatherViewModel
import ai.andromeda.weather.shared.WeatherViewModelFactory
import android.util.Log
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.week_fragment.*
import kotlinx.android.synthetic.main.week_fragment.view.*
import java.lang.Exception
import java.util.*

class WeekFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var rootView: View

    private val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

    private val weekDays = mutableListOf(
        R.id.weekDay0,
        R.id.weekDay1,
        R.id.weekDay2,
        R.id.weekDay3,
        R.id.weekDay4,
        R.id.weekDay5,
    )

    private val dayNames = mutableListOf(
        R.id.day0Name,
        R.id.day1Name,
        R.id.day2Name,
        R.id.day3Name,
        R.id.day4Name,
        R.id.day5Name
    )

    private val dayWeatherIcons = mutableListOf(
        R.id.day0WeatherIcon,
        R.id.day1WeatherIcon,
        R.id.day2WeatherIcon,
        R.id.day3WeatherIcon,
        R.id.day4WeatherIcon,
        R.id.day5WeatherIcon
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.week_fragment, container, false)
        addOnClickListeners()
        selectDay(weekDays[0])
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = WeatherViewModelFactory(application)
        viewModel = ViewModelProvider(requireNotNull(this.activity), viewModelFactory)
            .get(WeatherViewModel::class.java)

        setDyaNames()
        setDayIcons()
        updateUI(index = 0)
    }

    private fun updateUI(index: Int) {
        val weather = viewModel.weather.value
        weather?.let {
            try {
                val day = weather.daily[index + 1]
                val icon = resources.getIdentifier(
                    "ic_${day.status[0].icon}",
                    "drawable",
                    BuildConfig.APPLICATION_ID
                )
                val weatherDescription = day.status[0].condition
                val min = day.temp.min
                val max = day.temp.max
                val morn = day.temp.morn
                val eve = day.temp.eve
                val night = day.temp.night

                rootView.weekDay.text = getDayOfWeek(today + index)
                rootView.forecastIcon.setImageResource(icon)
                rootView.weatherDescription.text = weatherDescription
                rootView.temperatureText.text =
                    getString(R.string.high_low_temp, max, min)
                rootView.morningTemperature.text =
                    getString(R.string.temperature_text, morn)
                rootView.afternoonTemperature.text =
                    getString(R.string.temperature_text, eve)
                rootView.nightTemperature.text =
                    getString(R.string.temperature_text, night)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setDyaNames() {
        for (i in 0..5) {
            rootView.findViewById<TextView>(dayNames[i]).text =
                getDayOfWeek(today + i)
        }
    }

    private fun setDayIcons() {
        val weather = viewModel.weather.value
        weather?.let {
            try {
                val icons = weather.daily.map {
                    resources.getIdentifier(
                        "ic_${it.status[0].icon}",
                        "drawable",
                        BuildConfig.APPLICATION_ID
                    )
                }
                dayWeatherIcons.forEachIndexed { index, day ->
                    rootView.findViewById<ImageView>(day).setImageResource(
                        icons[index + 1]
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun addOnClickListeners() {
        for (id in weekDays) {
            rootView.findViewById<RelativeLayout>(id).setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        selectDay(v.id)
        updateUI(weekDays.indexOf(v.id))
    }

    private fun clearSelection() {
        for (id in weekDays) {
            rootView.findViewById<RelativeLayout>(id).background =
                ContextCompat.getDrawable(rootView.context, R.drawable.outlined_card)
        }
    }

    private fun selectDay(id: Int) {
        clearSelection()
        rootView.findViewById<RelativeLayout>(id).background =
            ContextCompat.getDrawable(rootView.context, R.drawable.outlined_card_selected)
    }
}