package com.bholmes.fizzbuzz

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

class FizzDetailFragment(fizzData: FizzData) : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fizz_detail, container, false)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fizz_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}