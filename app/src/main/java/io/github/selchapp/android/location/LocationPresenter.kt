package io.github.selchapp.android.location

import android.util.Log
import com.fernandocejas.frodo.annotation.RxLogObservable
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.github.selchapp.android.retrofit.SelchApiService
import io.github.selchapp.android.retrofit.BasiAuthInterceptor
import io.github.selchapp.android.retrofit.model.GPRSPosition
import io.github.selchapp.android.retrofit.model.Route
import io.github.selchapp.android.retrofit.model.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor


/**
 * Created by rzetzsche on 30.09.17.
 */
class LocationPresenter(val view: MapContract.View) : MapContract.Presenter {
    override fun getRoute(id: Int, lastLoc: GPRSPosition) {
        service.getRoute(id, lastLoc).enqueue(object : Callback<Route> {
            override fun onFailure(call: Call<Route>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<Route>?, response: Response<Route>?) {
                for (step in response?.body()!!.steps) {
                    for (gprsPosition in step.paths) {
                        Log.e("Test", "${gprsPosition.lat} ${gprsPosition.lng}")
                    }
                }
//                view.renderRoute(response?.body()!!);
            }

        })
    }

    val service: SelchApiService

    init {


        val intercept = HttpLoggingInterceptor()
        intercept.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .addInterceptor(BasiAuthInterceptor("test", "test"))
                .addInterceptor(intercept)
                .build()
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl("http://10.1.228.148:15234")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        service = retrofit.create<SelchApiService>(SelchApiService::class.java)
        view.setPresenter(this)
    }

    override fun updateTeamMember(id: Int) {

        getObservable(id)
                .subscribe({ view.showTeamMember(it.second, it.first) })
    }

    @RxLogObservable(RxLogObservable.Scope.STREAM)
    private fun getObservable(id: Int): Observable<Pair<GPRSPosition, User>> {
        return service.getTeam(id)
                .flatMap { Observable.fromIterable(it.users) }
                .flatMap { Observable.combineLatest(service.getPosition(it), service.getUser(it), BiFunction { pos: GPRSPosition, user: User -> Pair(pos, user) }) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}