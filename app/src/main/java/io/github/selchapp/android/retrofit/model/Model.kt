package io.github.selchapp.android.retrofit.model

/**
 * Created by rzetzsche on 30.09.17.
 */

class Team(val id: Long, var name: String, var users: Collection<Int>)

class GPRSPosition(val latitude: Double, val longitude: Double)
class User(val id: Int, val nickname: String, val teams: Collection<Int>)
class Step(val instruction: String, val distance: Int
           , val start: GPRSPosition, val end: GPRSPosition, val path: Collection<GPRSPosition>)

class Route(val steps: Collection<Step>)
