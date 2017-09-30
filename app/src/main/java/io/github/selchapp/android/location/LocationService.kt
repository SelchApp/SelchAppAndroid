package io.github.selchapp.android.location

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.OnCompleteListener


/**
 * Created by rzetzsche on 30.09.17.
 */
class LocationService : Service() {

    private val COMMAND = "COMMAND"
    private val TAG = LocationService::class.java.simpleName
    private var mFusedLocationClient: FusedLocationProviderClient? = null


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        mFusedLocationClient = FusedLocationProviderClient(applicationContext)
        super.onCreate()
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        mFusedLocationClient!!.lastLocation.addOnCompleteListener({
            Log.e(TAG, it.result.toString())
        })
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}