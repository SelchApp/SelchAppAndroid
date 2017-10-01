package io.github.selchapp.android.location

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import io.github.selchapp.android.R
import io.github.selchapp.android.retrofit.model.GPRSPosition
import io.github.selchapp.android.retrofit.model.Route
import io.github.selchapp.android.retrofit.model.User
import io.github.selchapp.android.voice.SpeechRecognitionService
import io.github.selchapp.android.voice.TextToSpeechService
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotterknife.bindView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import io.github.selchapp.android.retrofit.model.Step
import org.osmdroid.views.overlay.Polyline
import java.util.regex.Pattern


/**
 * Created by rzetzsche on 30.09.17.
 */
class LocationFragment : Fragment(), MapContract.View, Consumer<Location>, SpeechRecognitionService.RecognizeListener {
    var mOverlay: ItemizedOverlayWithFocus<OverlayItem>? = null
    val mapView: MapView by bindView(R.id.mapView)
    var lastLoc: GPRSPosition? = null
    var lines: ArrayList<Polyline> = ArrayList()
    lateinit var pres: MapContract.Presenter
    lateinit var geoFenceClient: GeofencingClient
    val regex = "bring me to (robert|valentin|stephan)"
    private lateinit var subscription: Disposable

    var i: Int = 0
    override fun renderRoute(route: Route) {
        mapView.overlayManager.removeAll(lines)
        lines.clear()
        for (step in route.steps) {
            setGeofence(step)
            val line = Polyline()
            if (step.type == "piste") line.color = Color.BLACK
            else line.color = Color.BLUE

            line.points = step.getGeoPoints()
            line.setOnClickListener { polyline, mapView, eventPos ->
                Toast.makeText(mapView.context, step.instructions, Toast.LENGTH_LONG).show()
                false
            }
            lines.add(line)
            mapView.overlayManager.add(line)
            mapView.invalidate()
        }
    }

    @SuppressLint("MissingPermission")
    private fun setGeofence(step: Step) {
        val point = step.path.first()
        Log.e("TEST", "${point.lat} ${point.lng}")
        val request = GeofencingRequest.Builder()
                .addGeofence(Geofence.Builder()
                        .setCircularRegion(point.lat, point.lng, 10f)
                        .setRequestId(i++.toString())
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                        .build())
        val intent = Intent(activity, TextToSpeechService::class.java)
        intent.putExtra("TEXT", step.instructions)
        geoFenceClient.addGeofences(request.build()
                , PendingIntent.getService(activity, i++, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    override fun wasRecognized() {
        pres.getRoute(4, GPRSPosition(47.480173, 12.192431))
    }


    val locationConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as LocationService.LocalBinder
            subscription = binder.getLocationSubject().subscribe(this@LocationFragment)
        }
    }

    val speechConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as SpeechRecognitionService.LocalBinder
            binder.setListener(this@LocationFragment)
        }
    }

    override fun showTeamMember(member: User, position: GPRSPosition) {
        addOverlay(member, position)
    }

    private fun addOverlay(member: User, position: GPRSPosition) {
        val items = ArrayList<OverlayItem>()
        items.add(OverlayItem(member.nickname, "", GeoPoint(position.lat, position.lng)))
        //the overlay
        val mOverlay = ItemizedOverlayWithFocus<OverlayItem>(activity, items,
                object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                    override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                        //do something
                        return true
                    }

                    override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
                        return false
                    }
                })
        mOverlay.setFocusItemsOnTap(true)
        mapView.overlays.add(mOverlay)
        mapView.invalidate()
    }

    override fun accept(p0: Location) {
        lastLoc = GPRSPosition(p0.latitude, p0.longitude)
        val items = ArrayList<OverlayItem>()
        items.add(OverlayItem("Dein Standort", "Lolol", GeoPoint(p0.latitude, p0.longitude)))
        //the overlay
        if (mOverlay != null) {
            mapView.overlays.remove(mOverlay)
        }
        mOverlay = ItemizedOverlayWithFocus<OverlayItem>(activity, items,
                object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                    override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                        //do something
                        return true
                    }

                    override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
                        return false
                    }
                })
        mOverlay!!.setFocusItemsOnTap(true)
        mapView.overlays.add(mOverlay)
        mapView.invalidate()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_map, null)
        geoFenceClient = LocationServices.getGeofencingClient(activity)
        return view
    }

    override fun onStart() {
        super.onStart()
        mapView.setMultiTouchControls(true)
        val mapController = mapView.controller
        mapController.setZoom(15)
        val startPoint = GeoPoint(47.480167, 12.192419)
        mapController.setCenter(startPoint)
    }

    override fun onPause() {
        super.onPause()
        activity.unbindService(locationConnection)
        subscription.dispose()
    }

    override fun onResume() {
        super.onResume()
        activity.bindService(Intent(activity, LocationService::class.java), locationConnection, Service.BIND_AUTO_CREATE)
    }

    override fun setPresenter(presenter: MapContract.Presenter) {
        pres = presenter
        pres.updateTeamMember(1)
    }

    fun listenToSpeech() {
        val listenIntent =
                Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                javaClass.getPackage().getName());
        listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a word!");
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        startActivityForResult(listenIntent, 1234);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        //check speech recognition result
        if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
            val words = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for (word in words) {
                if (regex.toRegex().matches(word.toLowerCase())) {
                    pres.getRoute(getIdFor(word.toLowerCase().removePrefix("bring me to "))
                            , GPRSPosition(lastLoc!!.lat, lastLoc!!.lng))
                }
            }
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getIdFor(removePrefix: String): Int {
        if (removePrefix == "robert") {
            return 3
        } else if (removePrefix == "stephan") {
            return 2
        } else if (removePrefix == "valentin") {
            return 4
        } else
            return 4
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        listenToSpeech()
        return super.onOptionsItemSelected(item)
    }

    fun Fragment.startService(intent: Intent) {
        activity.startService(intent)
    }
}