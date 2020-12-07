package ai.andromeda.weather.network

import ai.andromeda.weather.config.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenWeatherApi {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Config.BASE_URL)
        .build()

    val RetrofitService: OpenWeatherApiService by lazy {
        retrofit.create(OpenWeatherApiService::class.java)
    }
}