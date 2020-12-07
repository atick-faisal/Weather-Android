package ai.andromeda.weather.repository

import ai.andromeda.weather.config.Config
import ai.andromeda.weather.database.AppDatabase
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationRepository(context: Context) {
    private val database = AppDatabase(context)
    private val locationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val permissionNotGranted: Boolean =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED

    val location: LiveData<String> = database.getLocation()

    @SuppressLint("MissingPermission")
    suspend fun refreshLocation() {
        if (permissionNotGranted) return
        withContext(Dispatchers.IO) {
            locationClient.lastLocation.addOnSuccessListener {
                it?.let {
                    val location = "${it.latitude},${it.longitude}"
                    database.updateLocation(location)
                    Log.i(Config.LOG_TAG, "LOCATION: $location")
                }
            }
        }
    }
}