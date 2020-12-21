package com.bholmes.fizzbuzz

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.bholmes.fizzbuzz.view_utils.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_fizz_main_list.*
import kotlinx.android.synthetic.main.fragment_fizz_main_list.view.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class FizzListFragment : Fragment(), FizzDataAdapter.Listener {

    private val TAG = MainActivity::class.java.simpleName
    private val BASE_URL = "http://static.navigamobile.com/fizzbuzz/"
    private var mCompositeDisposable: CompositeDisposable? = null
    private var mFizzDataArrayList: ArrayList<FizzData>? = null
    private var mAdapterFizz: FizzDataAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fizz_main_list, container, false)
        view.recycler_view.setHasFixedSize(true)
        mCompositeDisposable = CompositeDisposable()
        loadJSON()
        return view
    }


    private fun loadJSON() {

        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(NetworkInterface::class.java)

//        Request initial list and get a disposable object in return, add this object
//        to our composite disposables
        mCompositeDisposable?.add(requestInterface.getData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse, this::handleError))
    }

    private fun handleResponse(response: Response<List<FizzData>>) {

        mFizzDataArrayList = response.body()!! as ArrayList<FizzData>
        mAdapterFizz = FizzDataAdapter(mFizzDataArrayList!!, this, context!!)

        recycler_view.adapter = mAdapterFizz
    }

    private fun handleError(error: Throwable) {
        Log.d(TAG, error.localizedMessage)
        "Error ${error.localizedMessage}".toast(context!!)
    }


    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    override fun onItemClick(fizzData: FizzData) {
        "${fizzData.title} Clicked !".toast(context!!)
    }
}