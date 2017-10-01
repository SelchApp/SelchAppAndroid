package io.github.selchapp.android.location

import android.app.Activity
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import io.github.selchapp.android.R

/**
 * Created by rzetzsche on 30.09.17.
 */
class MapActivity : Activity(), ServiceConnection {
    var fragment: LocationFragment? = null

    override fun onServiceDisconnected(p0: ComponentName?) {
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        fragment = fragmentManager
                .findFragmentById(R.id.fragmentMap) as LocationFragment
        val presenter = LocationPresenter(fragment!!)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val actionItem = menu.add("Mic")
        actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        actionItem.setIcon(R.drawable.ic_mic_white_24dp)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        fragment?.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }
}