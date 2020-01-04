package com.kolo.gorskih.tica.imagine.ui.edit

/**
 * Shows a list of Emoji in a grid.
 */

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kolo.gorskih.tica.imagine.R
import kotlinx.android.synthetic.main.item_emoji.view.*

class EmojiAdapter(
    private val onEmojiSelected: (String) -> Unit
) : RecyclerView.Adapter<EmojiViewHolder>() {

    private val items = mutableListOf<String>()

    fun setData(newItems: List<String>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        holder.showData(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_emoji, parent, false)

        return EmojiViewHolder(view, onEmojiSelected)
    }
}

class EmojiViewHolder(
    itemView: View,
    private val onEmojiSelected: (String) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    fun showData(emoji: String) = with(itemView) {
        emojiText.text = emoji

        setOnClickListener { onEmojiSelected(emoji) }
    }
}