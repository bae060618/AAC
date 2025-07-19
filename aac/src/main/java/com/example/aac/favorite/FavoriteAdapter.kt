package com.example.aac.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aac.R

class FavoriteAdapter(
    private var items: MutableList<FavoriteItem>,
    private val onRemoveClick: (FavoriteItem) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.itemIcon)
        val label: TextView = view.findViewById(R.id.itemLabel)
        val deleteIcon: ImageView = view.findViewById(R.id.itemDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.iconResId)
        holder.label.text = item.label
        holder.deleteIcon.setOnClickListener {
            onRemoveClick(item)
        }
    }

    fun removeItem(item: FavoriteItem) {
        items.remove(item)
        notifyDataSetChanged()
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }
}
