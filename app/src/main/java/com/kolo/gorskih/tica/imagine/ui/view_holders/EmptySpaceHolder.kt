package com.kolo.gorskih.tica.imagine.ui.view_holders

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.deliverytest.base.epoxy.KotlinHolder
import com.kolo.gorskih.tica.imagine.R

@EpoxyModelClass(layout = R.layout.cell_empty_space)
abstract class EmptySpaceModelHolder : EpoxyModelWithHolder<EmptySpaceHolder>()
class EmptySpaceHolder : KotlinHolder()