package ai.andromeda.weather.home

import ai.andromeda.weather.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.home_fragment.view.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

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
}