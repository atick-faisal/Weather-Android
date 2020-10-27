package ai.andromeda.weather.network

import ai.andromeda.weather.config.Config
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {
    @GET("weather")
    fun fetch(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = Config.API_KEY
    ): Call<Weather>
}