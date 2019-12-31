package com.kolo.gorskih.tica.imagine.ui.edit

/**
 * TODO - Add comment
 */

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kolo.gorskih.tica.imagine.R
import ja.burhanrashid52.photoeditor.PhotoFilter
import kotlinx.android.synthetic.main.item_photo_filter.view.*

class PhotoFilterAdapter(
    private val onFilterSelected: (PhotoFilter) -> Unit
) : RecyclerView.Adapter<PhotoFilterViewHolder>() {

    private val items = mutableListOf<PhotoFilter>()

    fun setData(newItems: List<PhotoFilter>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PhotoFilterViewHolder, position: Int) {
        holder.showData(items[position], onFilterSelected)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoFilterViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_photo_filter, parent, false)

        return PhotoFilterViewHolder(view)
    }
}

class PhotoFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun showData(
        photoFilter: PhotoFilter,
        onFilterSelected: (PhotoFilter) -> Unit
    ) = with(itemView) {
        filterOption.text = photoFilter
            .name
            .let {
                val text = it
                    .replace("_", " ")
                    .toLowerCase()

                val firstLetter = text.first().toUpperCase()

                firstLetter + text.substring(1, text.length)
            }

        setOnClickListener { onFilterSelected(photoFilter) }
    }
}