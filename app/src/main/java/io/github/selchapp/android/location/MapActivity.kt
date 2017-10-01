package io.github.selchapp.android.location

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import io.github.selchapp.android.R
import io.github.selchapp.android.voice.SpeechRecognitionService


/**
 * Created by rzetzsche on 30.09.17.
 */
class MapActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val fragment = fragmentManager
                .findFragmentById(R.id.fragmentMap) as LocationFragment
        val presenter = LocationPresenter(fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val actionItem = menu.add("Mic")
        actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        actionItem.setIcon(R.drawable.ic_mic_white_24dp)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title == "Mic")
            startService(Intent(applicationContext, SpeechRecognitionService::class.java))
        return super.onOptionsItemSelected(item)
    }
}