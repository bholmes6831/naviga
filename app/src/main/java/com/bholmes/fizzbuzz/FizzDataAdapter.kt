package com.bholmes.fizzbuzz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fizz_item_view.view.*

class FizzDataAdapter (private val dataList : ArrayList<FizzData>, private val listener : Listener, var context: Context)
    : RecyclerView.Adapter<FizzDataAdapter.FizzDataViewHolder>() {

    val IMAGE_DIMENSION = 80

    interface Listener {
        fun onItemClick(fizzData : FizzData)
    }

    override fun onBindViewHolder(holder: FizzDataViewHolder, position: Int) {
        holder.bind(dataList[position], listener, position)
    }

    override fun getItemCount(): Int = dataList.count()

    /**
     * parent - recyclerview
     * viewType - not needed for this list
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FizzDataViewHolder {
        // Always pass context of parent view group in recyclerview adapter for inflating ViewHolders.
        // attachToRoot is false, rv attaches this to view hierarchy for us
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fizz_item_view, parent, false)
        return FizzDataViewHolder(view)
    }

    inner class FizzDataViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        // Keep view logic attached to viewHolder class
        fun bind(fizzData: FizzData, listener: Listener, position: Int) {
            itemView.title.text = fizzData.title
            if(fizzData.media) {
                itemView.media.visibility = View.VISIBLE
            } else {
                itemView.media.visibility = View.GONE
            }

            if(fizzData.image?.url != null) {
                Glide.with(context)
                    .load(fizzData.image?.url)
                    .override(getPixels(IMAGE_DIMENSION), getPixels(IMAGE_DIMENSION))
                    .centerCrop()
                    .into(itemView.image)
                itemView.image.visibility = View.VISIBLE
            } else {
                itemView.image.visibility = View.GONE
            }

            itemView.setOnClickListener{ listener.onItemClick(fizzData) }
        }
    }

    fun getPixels (dps: Int) : Int {
        val scale: Float = context.getResources().getDisplayMetrics().density
        val pixels = (dps * scale + 0.5f).toInt()
        return pixels
    }
}