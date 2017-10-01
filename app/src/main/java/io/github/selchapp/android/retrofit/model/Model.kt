package io.github.selchapp.android.retrofit.model

import org.osmdroid.util.GeoPoint
import kotlin.collections.ArrayList

/**
 * Created by rzetzsche on 30.09.17.
 */

class Team(val id: Long, var name: String, var users: Collection<Int>)

class GPRSPosition(val lat: Double, val lng: Double) {
    fun toGeoPoint(): GeoPoint {
        return GeoPoint(lat, lng)
    }
}

class User(val id: Int, val nickname: String, val teams: Collection<Int>)
class Step(val path: Collection<GPRSPosition>, val instructions: String, val type: String) {
    fun getGeoPoints(): List<GeoPoint> {
        val list = ArrayList<GeoPoint>()
        for (point in path) {
            list.add(point.toGeoPoint())
        }
        return list
    }
}

class Route(val steps: Collection<Step>)
