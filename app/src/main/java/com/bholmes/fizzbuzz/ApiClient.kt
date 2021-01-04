package com.bholmes.fizzbuzz

import android.util.Log
import android.view.View
import com.bholmes.fizzbuzz.view_utils.toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_fizz_main_list.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

object ApiClient {
    private val networkInterface: NetworkInterface

    init {
        val retrofit =  Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        networkInterface = retrofit.create(NetworkInterface::class.java)
    }

    fun getMainList() : Observable<Response<List<FizzData>>> {
        return networkInterface.getMainList()
    }

    fun getDetail(id: String) : Observable<Response<FizzDetail>> {
        return networkInterface.getDetail("$id")
    }
}