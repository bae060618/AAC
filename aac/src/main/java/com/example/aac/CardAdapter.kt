package com.example.aac

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.aac.favorite.FavoriteItem

class CardAdapter(
    private val items: List<CardItem>,
    private val onClick: (CardItem) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.categoryIcon)
        val label: TextView = view.findViewById(R.id.categoryLabel)
        val btnFavorite: ImageView = view.findViewById(R.id.btnFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.iconResId)
        holder.label.text = item.label

        holder.itemView.setOnClickListener { onClick(item) }

        holder.btnFavorite.setOnClickListener {
            val context = holder.itemView.context
            val favoriteItem = FavoriteItem(item.label, item.iconResId, "")
            com.example.aac.favorite.FavoriteRepository.addFavorite(context, favoriteItem)
        }
    }

    override fun getItemCount(): Int = items.size
}

