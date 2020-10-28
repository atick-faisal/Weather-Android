package ai.andromeda.weather

import ai.andromeda.weather.config.Config
import ai.andromeda.weather.network.OpenWeatherApi
import ai.andromeda.weather.network.Weather
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
