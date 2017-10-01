package io.github.selchapp.android.location

import android.app.Fragment
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.Bundle
import android.os.IBinder
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

/**
 * Created by rzetzsche on 30.09.17.
 */
class LocationFragment : Fragment(), MapContract.View, Consumer<Location>, SpeechRecognitionService.RecognizeListener {
    override fun renderRoute(route: Route) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun wasRecognized() {
        val intent = Intent(activity, TextToSpeechService::class.java)
        // intent.putExtra("TEXT", "Und dann bin ich der Assoziale? Die sind die Assozialen")
        startService(intent)
        pres.getRoute(2, lastLoc!!)
    }

    val mapView: MapView by bindView(R.id.mapView)
    var lastLoc: GPRSPosition? = null
    lateinit var pres: MapContract.Presenter

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
        lastLoc = position
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
    }

    override fun accept(p0: Location) {

        val items = ArrayList<OverlayItem>()
        items.add(OverlayItem("Dein Standort", "Lolol", GeoPoint(p0.latitude, p0.longitude)))
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
    }

    private lateinit var subscription: Disposable


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_map, null)
        return view
    }

    override fun onStart() {
        super.onStart()
        mapView.setMultiTouchControls(true)
        val mapController = mapView.controller
        mapController.setZoom(9)
        val startPoint = GeoPoint(47.5617131, 12.2931463)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title == "Mic")
            pres.getRoute(2, lastLoc!!)

        //    activity.bindService(Intent(activity, SpeechRecognitionService::class.java), speechConnection, Service.BIND_AUTO_CREATE)
        return super.onOptionsItemSelected(item)
    }

    fun Fragment.startService(intent: Intent) {
        activity.startService(intent)
    }
}