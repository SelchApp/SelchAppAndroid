package io.github.selchapp.android.retrofit.model

/**
 * Created by rzetzsche on 30.09.17.
 */

class Team(val id: Long, var name: String, var users: Collection<Int>)

class GPRSPosition(val lat: Double, val lng: Double)
class User(val id: Int, val nickname: String, val teams: Collection<Int>)
class Step(val paths: Collection<GPRSPosition>)
class Route(val steps: Collection<Step>)
