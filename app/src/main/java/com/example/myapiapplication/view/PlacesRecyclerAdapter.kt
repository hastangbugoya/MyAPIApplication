package com.example.myapiapplication.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapiapplication.R
import com.example.myapiapplication.model.Result

class PlacesRecyclerAdapter : RecyclerView.Adapter<PlacesRecyclerAdapter.PlacesViewHolder>() {

    private var placesList: List<Result> = mutableListOf()

    fun updateList(list: MutableList<Result>) {
        placesList = list
        notifyDataSetChanged()
    }

    inner class PlacesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.place_item_layout, parent, false)
        return PlacesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        val cuurentPlace: Result = placesList[position]
        cuurentPlace.let {
            holder.itemView.apply {
                this.findViewById<TextView>(R.id.place_item_name).text = it.name
                this.findViewById<TextView>(R.id.place_item_user_rating_textview).text = String.format(context.getString(R.string.ratings_format),it.rating)
            }
        }
    }

    override fun getItemCount(): Int = placesList.size

}