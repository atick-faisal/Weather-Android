package ai.andromeda.weather.network

import ai.andromeda.weather.config.Config
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {
    @GET("onecall")
    suspend fun fetch(
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
            @Query("appid") apiKey: String = Config.API_KEY,
            @Query("exclude") exclude: String = "minutely,daily",
            @Query("units") unit: String = "metric",
            @Query("lang") lang: String = "en"
    ): Weather?
}