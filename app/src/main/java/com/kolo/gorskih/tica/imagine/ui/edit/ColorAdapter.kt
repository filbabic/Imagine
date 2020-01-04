package com.kolo.gorskih.tica.imagine.ui.edit

/**
 * Displays colors for the paint brush.
 */

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.kolo.gorskih.tica.imagine.R
import kotlinx.android.synthetic.main.item_color.view.*

class ColorAdapter(private val onColorPicked: (Int) -> Unit) :
    RecyclerView.Adapter<ColorViewHolder>() {

    private val items = mutableListOf<Int>()

    fun setData(newItems: List<Int>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.showData(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)

        return ColorViewHolder(view, onColorPicked)
    }
}

class ColorViewHolder(
    itemView: View,
    private val onColorPicked: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    fun showData(@ColorInt color: Int) = with(itemView) {
        val gradientDrawable = colorOption.background as? GradientDrawable ?: return
        val mutated =
            gradientDrawable
                .constantState
                ?.newDrawable()
                ?.mutate() as? GradientDrawable ?: return

        mutated.setColor(color)

        colorOption.background = mutated

        setOnClickListener { onColorPicked(color) }
    }
}