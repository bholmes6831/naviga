package com.bholmes.fizzbuzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), DataAdapter.Listener {

    private val TAG = MainActivity::class.java.simpleName
    private val BASE_URL = "http://static.navigamobile.com/fizzbuzz/"
    private var mCompositeDisposable: CompositeDisposable? = null
    private var mFizzDataArrayList: ArrayList<FizzData>? = null
    private var mAdapter: DataAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCompositeDisposable = CompositeDisposable()

        initRecyclerView()

        loadJSON()
    }

    private fun initRecyclerView() {

        rv_android_list.setHasFixedSize(true)
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        rv_android_list.layoutManager = layoutManager
    }

    private fun loadJSON() {

        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(NetworkInterface::class.java)

        mCompositeDisposable?.add(requestInterface.getData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse, this::handleError))
    }

    private fun handleResponse(response: Response<List<FizzData>>) {

        mFizzDataArrayList = response.body()!! as ArrayList<FizzData>
        mAdapter = DataAdapter(mFizzDataArrayList!!, this)

        rv_android_list.adapter = mAdapter
    }

    private fun handleError(error: Throwable) {

        Log.d(TAG, error.localizedMessage)

        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(fizzData: FizzData) {

        Toast.makeText(this, "${fizzData.title} Clicked !", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }
}

