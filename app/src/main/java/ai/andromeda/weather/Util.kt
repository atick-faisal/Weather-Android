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

fun getDayOfWeek(id: Int): String {
    return when(id % 7) {
        0 -> "Sunday"
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6 -> "Saturday"
        else -> "Sunday"
    }
}

fun getCurrentDayOfWeek(): String {
    return when(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
        Calendar.SUNDAY -> "Sunday"
        Calendar.MONDAY -> "Monday"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        else -> "Sunday"
    }
}