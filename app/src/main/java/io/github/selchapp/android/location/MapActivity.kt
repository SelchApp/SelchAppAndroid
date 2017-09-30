package io.github.selchapp.android.location

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import io.github.selchapp.android.R
import kotterknife.bindView
import org.osmdroid.views.MapView

/**
 * Created by rzetzsche on 30.09.17.
 */
class MapActivity : Activity(), MapContract.View {
    val mapView: MapView by bindView(R.id.mapView)

    override fun setPresenter(presenter: MapContract.Presenter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
    }
}