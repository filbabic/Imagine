package com.kolo.gorskih.tica.imagine.ui.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.example.deliverytest.base.epoxy.KotlinHolder
import com.google.firebase.storage.StorageReference
import com.kolo.gorskih.tica.imagine.R

@EpoxyModelClass(layout = R.layout.cell_image_item)
abstract class ImageItemModelHolder : EpoxyModelWithHolder<ImageItemHolder>() {
    @EpoxyAttribute
    lateinit var imageTitle: String
    @EpoxyAttribute
    lateinit var imageURL: StorageReference

    @EpoxyAttribute
    lateinit var listener: (StorageReference) -> Unit

    @EpoxyAttribute lateinit var longClickListener : View.OnLongClickListener

    override fun bind(holder: ImageItemHolder) {
        holder.title.text = imageTitle
        Glide.with(holder.image).load(imageURL).centerCrop().into(holder.image)

        holder.image.setOnClickListener {
            listener(imageURL)
        }
    }
}

class ImageItemHolder : KotlinHolder() {
    val title by bind<TextView>(R.id.tvImageTitle)
    val image by bind<ImageView>(R.id.ivImage)
}