package io.github.selchapp.android.location

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import com.google.android.gms.location.*
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject


/**
 * Created by rzetzsche on 30.09.17.
 */
class LocationService : Service() {

    private val COMMAND = "COMMAND"
    private val TAG = LocationService::class.java.simpleName
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    val subject: BehaviorSubject<Location> = BehaviorSubject.create()


    override fun onBind(intent: Intent): IBinder? {
        return LocalBinder(subject)
    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        mFusedLocationClient = FusedLocationProviderClient(applicationContext)
        val request = LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(10000)
                .setSmallestDisplacement(0f)
        mFusedLocationClient!!.requestLocationUpdates(request
                , CustomLocationCallback(subject), Looper.myLooper())
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    inner class LocalBinder(val subject: Subject<Location>) : Binder() {
        fun getLocationSubject(): Subject<Location> {
            return subject
        }

        fun getApiClient(): FusedLocationProviderClient? {
            return this@LocationService.mFusedLocationClient
        }
    }

    class CustomLocationCallback(val subject: BehaviorSubject<Location>) : LocationCallback() {


        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            subject.onNext(p0.lastLocation)
        }

        override fun onLocationAvailability(p0: LocationAvailability?) {
            super.onLocationAvailability(p0)
        }
    }

}