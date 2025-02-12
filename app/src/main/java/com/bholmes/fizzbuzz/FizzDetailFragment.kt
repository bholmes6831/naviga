package com.bholmes.fizzbuzz

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.bholmes.fizzbuzz.view_utils.toast
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fizz_item_view.view.*
import kotlinx.android.synthetic.main.fragment_fizz_detail.*
import kotlinx.android.synthetic.main.fragment_fizz_detail.view.*
import kotlinx.android.synthetic.main.fragment_fizz_main_list.*

class FizzDetailFragment() : Fragment(){

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private var url: String? = null
    private var detailID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setFragmentResultListener("detailListener") { requestKey, bundle ->
            loadBundle(bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fizz_detail, container, false)
        view.toolbar.setNavigationOnClickListener { view ->
            activity?.supportFragmentManager?.popBackStack()
        }
        view.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_share -> {
                    //TODO
                    "Share action clicked".toast(requireContext())
                    true
                }
                else -> false
            }
        }
        return view
    }

    private fun loadBundle(bundle: Bundle) {
        detailID = bundle.getInt("detailID")
        if(bundle.containsKey("url")) {
            url = bundle.getString("url")
            setImage(url)
        }
        loadDetail(detailID!!)
    }

    private fun setImage(url: String?) {
        Glide.with(requireContext())
            .load(url)
            .into(appbar_image)
    }

    private fun loadDetail(id: Int) {
        mCompositeDisposable.add(ApiClient.getDetail("$id")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                val item = response?.body()
                toolbar_layout.title = item?.title
                description.text = item?.body
            }, {error ->
                "Error ${error.localizedMessage}".toast(requireContext())
            }))
    }

    /**
     * Called as soon as app is put into background, before onDestroy()
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("detailID", detailID!!)
        if(url != null) {
            outState.putString("url", url)
        }
    }

    /**
     * Called after onStart()
     */
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null) {
            loadBundle(savedInstanceState)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }
}