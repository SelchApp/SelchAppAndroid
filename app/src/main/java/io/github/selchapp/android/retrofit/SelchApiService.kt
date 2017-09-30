package io.github.selchapp.android.retrofit


import io.github.selchapp.android.retrofit.model.GPRSPosition
import io.github.selchapp.android.retrofit.model.Route
import io.github.selchapp.android.retrofit.model.User
import retrofit2.Call
import retrofit2.http.*

interface SelchApiService {
    @GET("position/{userId}")
    fun getPosition(@Path("userId") userId: Int): Call<GPRSPosition>

    @PUT("position/self/{gprsPosition}")
    fun putPosition(@Path("gprsPosition") gprsPosition: GPRSPosition): Call<GPRSPosition>

    @GET("route/user/{userId}/{gprsPosition}")
    fun getRoute(@Path("userId") userId: Int, @Path("gprsPosition") gprsPosition: GPRSPosition): Call<Route>

    @GET("team/{teamId}")
    fun getTeamMember(@Path("teamId") teamId: Int): Call<Collection<User>>

    @GET("user/{userId}")
    fun getUser(@Path("userId") userId: Int): Call<User>

}