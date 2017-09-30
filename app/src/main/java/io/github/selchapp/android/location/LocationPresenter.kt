package io.github.selchapp.android.location

import io.github.selchapp.android.retrofit.SelchApiService
import io.github.selchapp.android.BasePresenter
import io.github.selchapp.android.retrofit.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


/**
 * Created by rzetzsche on 30.09.17.
 */
class LocationPresenter(val view: MapContract.View) : BasePresenter, MapContract.Presenter {


    lateinit var service: SelchApiService
    override fun updateTeamMember(id: Int) {
        service.getTeamMember(id).enqueue(object : Callback<Collection<User>> {
            override fun onResponse(call: Call<Collection<User>>, response: Response<Collection<User>>) {
                view.showTeamMember(response.body()!!)
            }

            override fun onFailure(call: Call<Collection<User>>, t: Throwable) {
            }
        })
    }

    override fun start() {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://10.1.228.148")
                .build()

        service = retrofit.create<SelchApiService>(SelchApiService::class.java)
    }

}