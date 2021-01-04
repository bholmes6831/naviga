package com.bholmes.fizzbuzz

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkInterface {
    @GET("fizzbuzz.json.gz")
    fun getMainList(): Observable<Response<List<FizzData>>>

    @GET("id/{id}.json.gz")
    fun getDetail(@Path("id") id: String): Observable<Response<FizzDetail>>
}