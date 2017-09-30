package io.github.selchapp.android.location

import io.github.selchapp.android.BaseView
import io.github.selchapp.android.retrofit.model.User
import org.osmdroid.views.MapController

/**
 * Created by rzetzsche on 30.09.17.
 */
interface MapContract {
    interface View : BaseView<Presenter> {
        fun showTeamMember(member: Collection<User>)
    }


    interface Presenter {
        fun updateTeamMember(id: Int)

    }
}