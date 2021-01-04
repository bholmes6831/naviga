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

class FizzDetailFragment(fizzData: FizzData) : Fragment(){

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setFragmentResultListener("detailListener") { requestKey, bundle ->
            val result = bundle.getInt("detailID")
            if(bundle.containsKey("url")) {
                setImage(bundle.getString("url")!!)
            }
            loadDetail(result)
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

    fun setImage(url: String) {
        Glide.with(requireContext())
            .load(url)
            .into(appbar_image)
    }

    fun loadDetail(id: Int) {
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


    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }
}