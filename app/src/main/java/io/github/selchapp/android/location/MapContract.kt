package io.github.selchapp.android.location

import io.github.selchapp.android.BaseView
import io.github.selchapp.android.retrofit.model.GPRSPosition
import io.github.selchapp.android.retrofit.model.Route
import io.github.selchapp.android.retrofit.model.User
import org.osmdroid.views.MapController

/**
 * Created by rzetzsche on 30.09.17.
 */
interface MapContract {
    interface View : BaseView<Presenter> {
        fun showTeamMember(member: User, position: GPRSPosition)
        fun renderRoute(route: Route)
    }


    interface Presenter {
        fun updateTeamMember(id: Int)

        fun getRoute(id: Int, lastLoc: GPRSPosition)

    }
}