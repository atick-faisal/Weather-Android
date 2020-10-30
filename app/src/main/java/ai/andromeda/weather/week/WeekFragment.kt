package ai.andromeda.weather.week

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ai.andromeda.weather.R
import ai.andromeda.weather.config.Config
import android.util.Log
import kotlinx.android.synthetic.main.week_fragment.view.*

class WeekFragment : Fragment() {

    private lateinit var viewModel: WeekViewModel
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.week_fragment, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeekViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun weekDayClick(v: View) {

    }
}