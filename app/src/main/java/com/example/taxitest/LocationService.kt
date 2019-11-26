package com.example.taxitest

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

class LocationService(context: Context){

    private lateinit var locationServiceHandler: LocationServiceHandler
    private lateinit var looper: Looper
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationRequest: LocationRequest? = null
    private var appContext=context
       init {

        val thread = HandlerThread("LocationThread")
        thread.start()
        looper = thread.looper
        locationServiceHandler = LocationServiceHandler(looper)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appContext)
           locationRequest = LocationRequest()
           requestLocationUpdates()

    }

    inner class LocationServiceHandler constructor(val serviceLooper: Looper) : android.os.Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
          //  requestLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        if (havePermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, object: LocationCallback() {
                private val TAG_LAST_LOCATION_TIME = "TAG_LAST_LOCATION_TIME"
                override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                    super.onLocationAvailability(locationAvailability)
                }

                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    if (locationResult != null && locationResult.locations.size > 0 && locationResult.locations[0] != null) {
                        val fetchedLocation = locationResult.locations[0]
                    }
                }
            }, null)
        }
    }

    fun havePermission(context: Context, permissionName: String): Boolean {
        return try {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(context, permissionName) == PackageManager.PERMISSION_GRANTED
        } catch (exc: RuntimeException) {
            false
        }

    }

}