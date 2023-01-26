package com.samm.imagesaver.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        val card: CardView = itemView.findViewById(R.id.card)
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
        //holder.imageView.load(place[position].image)

        val context = holder.imageView.context
        val imageUrl = place[position].image
        val imageView = holder.imageView

        Glide.with(context)
            .load(imageUrl)
            .override(750, 500) // resizing
            .centerCrop()
            .into(imageView)

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