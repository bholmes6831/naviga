package com.bholmes.fizzbuzz

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET

interface NetworkInterface {
    @GET("fizzbuzz.json.gz")
    fun getData(): Observable<Response<List<FizzData>>>
}