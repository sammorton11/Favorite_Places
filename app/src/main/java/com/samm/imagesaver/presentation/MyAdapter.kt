package com.samm.imagesaver.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.samm.imagesaver.NewPlaceFragment
import com.samm.imagesaver.R
import com.samm.imagesaver.domain.Place

class MyAdapter(
    private val cardClick: OnCardClick
): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    private var place = arrayListOf<Place>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.title)
        val description: TextView = itemView.findViewById(R.id.description)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val card: ConstraintLayout = itemView.findViewById(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return place.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text
        holder.title.text = place[position].title
        holder.description.text = place[position].description
        holder.imageView.load(place[position].image)

        holder.card.setOnClickListener {
            cardClick.onCardClick(place[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(place: ArrayList<Place>){
        this.place = place
        notifyDataSetChanged()
    }

    interface OnCardClick {
        fun onCardClick(place: Place)
    }
}