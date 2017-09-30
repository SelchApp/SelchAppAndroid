package io.github.selchapp.android

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.github.selchapp.android.retrofit.SelchApiService
import io.github.selchapp.android.retrofit.model.Team
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import com.jayway.awaitility.Awaitility.await
import io.github.selchapp.android.retrofit.BasiAuthInterceptor
import io.github.selchapp.android.retrofit.model.GPRSPosition
import io.github.selchapp.android.retrofit.model.User
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import java.util.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testRetrofit() {
        val client = OkHttpClient.Builder()
                .addInterceptor(BasiAuthInterceptor("test", "test"))
                .build()
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl("http://10.1.228.148:15234")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        var result: Team? = null

        val service = retrofit.create<SelchApiService>(SelchApiService::class.java)

        service.getTeam(1)
                .flatMap { Observable.fromIterable(it.users) }
                .flatMap { Observable.combineLatest(service.getPosition(it), service.getUser(it), BiFunction { pos: GPRSPosition, user: User -> Pair(pos, user.nickname) }) }
                .subscribe({ println("${it.first} ${it.second}") })
//        service.getTeam(1).enqueue(object : Callback<Team> {
//            override fun onFailure(call: Call<Team>, t: Throwable) {
//                print(t.localizedMessage)
//            }
//
//            override fun onResponse(call: Call<Team>, response: Response<Team>) {
//                println(response)
//                result = response.body()!!
//            }
//
//        })
//        await().atMost(10, TimeUnit.SECONDS).until<Boolean>({
//            result != null;
//        })
//
//
//        assertNotNull(result)
    }
}
