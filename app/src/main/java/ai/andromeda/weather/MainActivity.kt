package ai.andromeda.weather

import ai.andromeda.weather.config.Config
import ai.andromeda.weather.network.OpenWeatherApi
import ai.andromeda.weather.network.Weather
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation.selectedItemId = R.id.homeFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigation.setupWithNavController(navController)

        val call = OpenWeatherApi
                .RetrofitService
                .fetch(
                        lat = 23.77,
                        lon = 90.42
                )

        call.enqueue(
                object : Callback<Weather> {
                    override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                        Log.i(Config.LOG_TAG, response.body().toString())
                    }

                    override fun onFailure(call: Call<Weather>, t: Throwable) {
                        Log.i(Config.LOG_TAG, "FAILED: ${t.message}")
                    }

                }
        )
    }
}
