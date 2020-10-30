package ai.andromeda.weather

import ai.andromeda.weather.config.Config
import java.util.*

fun getMorningIndex(): Int {
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    return (24 - currentHour + Config.MORNING) % 24
}

fun getAfternoonIndex(): Int {
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    return (24 - currentHour + Config.AFTERNOON) % 24
}

fun getNightIndex(): Int {
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    return (24 - currentHour + Config.NIGHT) % 24
}