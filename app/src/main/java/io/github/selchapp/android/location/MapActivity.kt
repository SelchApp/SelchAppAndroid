package io.github.selchapp.android.location

import android.app.Activity
import android.os.Bundle
import io.github.selchapp.android.R


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
}