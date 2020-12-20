package com.bholmes.fizzbuzz

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.recycler_view_row.view.*

class DataAdapter (private val dataList : ArrayList<FizzData>, private val listener : Listener, var context: Context) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

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

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        fun bind(fizzData: FizzData, listener: Listener, colors : Array<String>, position: Int) {

            itemView.title.text = fizzData.title

            if(fizzData.media) {
                itemView.media.visibility = View.VISIBLE
            } else {
                itemView.media.visibility = View.GONE
            }

            if(fizzData.image?.url != null) {
                Glide.with(context).load(fizzData.image?.url).into(itemView.image)
                itemView.image.visibility = View.VISIBLE
            } else {
                itemView.image.visibility = View.GONE
            }

            itemView.setOnClickListener{ listener.onItemClick(fizzData) }
        }
    }
}