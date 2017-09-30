package io.github.selchapp.android.retrofit.model

/**
 * Created by rzetzsche on 30.09.17.
 */

data class Team(val id: Long, var name: String, var users: Collection<User>)

data class GPRSPosition(val latitude: Double, val longitude: Double)
data class User(val id: Int, val nickname: String, val teams: Collection<Team>)
data class Step(val instruction: String, val distance: Int
                , val start: GPRSPosition, val end: GPRSPosition, val path: Collection<GPRSPosition>)

data class Route(val steps: Collection<Step>)
