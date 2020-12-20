package com.bholmes.fizzbuzz

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_view_row.view.*

class DataAdapter (private val dataList : ArrayList<FizzData>, private val listener : Listener) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    interface Listener {

        fun onItemClick(fizzData : FizzData)
    }

    private val colors : Array<String> = arrayOf("#EF5350", "#EC407A", "#AB47BC", "#7E57C2", "#5C6BC0", "#42A5F5")

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(dataList[position], listener, colors, position)
    }

    override fun getItemCount(): Int = dataList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        fun bind(fizzData: FizzData, listener: Listener, colors : Array<String>, position: Int) {

            itemView.tv_name.text = fizzData.title
//            itemView.tv_version.text = android.version
//            itemView.tv_api_level.text = android.apiLevel
            itemView.setBackgroundColor(Color.parseColor(colors[position % 6]))

            itemView.setOnClickListener{ listener.onItemClick(fizzData) }
        }
    }
}