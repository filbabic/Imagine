package com.kolo.gorskih.tica.imagine.ui.view_holders

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.example.deliverytest.base.epoxy.KotlinHolder
import com.kolo.gorskih.tica.imagine.R

@EpoxyModelClass(layout = R.layout.cell_image_item)
abstract class ImageItemModelHolder : EpoxyModelWithHolder<ImageItemHolder>(){
    @EpoxyAttribute lateinit var imageTitle : String
    @EpoxyAttribute lateinit var imageURL : String

    override fun bind(holder: ImageItemHolder) {
        holder.title.text = imageTitle
        Glide.with(holder.image).load(imageURL).centerCrop().into(holder.image)
    }
}

class ImageItemHolder : KotlinHolder(){
    val title by bind<TextView>(R.id.tvImageTitle)
    val image by bind<ImageView>(R.id.ivImage)
}