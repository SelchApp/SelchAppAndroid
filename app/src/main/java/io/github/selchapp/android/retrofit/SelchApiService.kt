package io.github.selchapp.android.retrofit


import io.github.selchapp.android.retrofit.model.GPRSPosition
import io.github.selchapp.android.retrofit.model.Route
import io.github.selchapp.android.retrofit.model.Team
import io.github.selchapp.android.retrofit.model.User
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface SelchApiService {
    @GET("position/user/{userId}")
    fun getPosition(@Path("userId") userId: Int): Observable<GPRSPosition>

    @PUT("position/self/{gprsPosition}")
    fun putPosition(@Path("gprsPosition") gprsPosition: GPRSPosition): Call<GPRSPosition>

    @POST("route/touser/{userId}")
    fun getRoute(@Path("userId") userId: Int, @Body gprsPosition: GPRSPosition): Call<Route>

    @GET("team/{teamId}")
    fun getTeam(@Path("teamId") teamId: Int): Observable<Team>

    @GET("user/{userId}")
    fun getUser(@Path("userId") userId: Int): Observable<User>

}