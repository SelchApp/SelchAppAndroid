package io.github.selchapp.android.location

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import io.github.selchapp.android.R
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotterknife.bindView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus


/**
 * Created by rzetzsche on 30.09.17.
 */
class MapActivity : Activity(), MapContract.View, ServiceConnection, Consumer<Location> {
    override fun accept(p0: Location) {
        val items = ArrayList<OverlayItem>()
        items.add(OverlayItem("Dein Standort", "Lolol", GeoPoint(p0.latitude, p0.longitude)))
        mapView.overlays.clear()
        //the overlay
        val mOverlay = ItemizedOverlayWithFocus<OverlayItem>(applicationContext, items,
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

    override fun onServiceDisconnected(p0: ComponentName?) {
    }

    private lateinit var subscription: Disposable

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as LocationService.LocalBinder
        subscription = binder.getLocationSubject().subscribe(this)
    }

    val mapView: MapView by bindView(R.id.mapView)

    override fun setPresenter(presenter: MapContract.Presenter) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        mapView.setMultiTouchControls(true)
        val mapController = mapView.controller
        mapController.setZoom(9)
        val startPoint = GeoPoint(47.5617131, 12.2931463)
        mapController.setCenter(startPoint)
    }

    override fun onPause() {
        super.onPause()
        unbindService(this)
        subscription.dispose()
    }

    override fun onResume() {
        super.onResume()
        bindService(Intent(this, LocationService::class.java), this, Service.BIND_AUTO_CREATE)
    }
}