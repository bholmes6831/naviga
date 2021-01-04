package com.bholmes.fizzbuzz

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.bholmes.fizzbuzz.view_utils.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_fizz_main_list.*
import kotlinx.android.synthetic.main.fragment_fizz_main_list.view.*


class FizzListFragment : Fragment(), FizzDataAdapter.Listener {

    private val TAG = MainActivity::class.java.simpleName
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private var mFizzDataArrayList: ArrayList<FizzData>? = null
    private var mAdapterFizz: FizzDataAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fizz_main_list, container, false)
        view.recycler_view.setHasFixedSize(true)
        setupToolbar(view)
        loadJSON()
        return view
    }

    private fun setupToolbar(view: View) {
        view.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.night_mode -> {
                    setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadJSON() {
//        Request initial list and get a disposable object in return, add this object
//        to our composite disposables
        mCompositeDisposable.add(ApiClient.getMainList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                mFizzDataArrayList = response.body()!! as ArrayList<FizzData>
                mAdapterFizz = FizzDataAdapter(mFizzDataArrayList!!, this, requireContext())
                recycler_view.adapter = mAdapterFizz
            }, {error ->
                Log.d(TAG, error.localizedMessage)
                "Error ${error.localizedMessage}".toast(requireContext())
            }))
    }

    //TODO
    private fun setNightMode(@AppCompatDelegate.NightMode nightMode: Int) {
        "Night Mode Clicked".toast(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }

    override fun onItemClick(fizzData: FizzData) {
        val bundle = bundleOf("detailID" to fizzData.id)
        if (fizzData?.image?.url != null) bundle.putString("url", fizzData?.image?.url)
        setFragmentResult("detailListener", bundle)
        (activity as NavigationHost).navigateTo(FizzDetailFragment(), true)
    }
}