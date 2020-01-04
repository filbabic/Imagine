package com.kolo.gorskih.tica.imagine.ui.edit

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.kolo.gorskih.tica.imagine.R
import kotlinx.android.synthetic.main.dialog_colors.*

/**
 * Allows you to pick a color for the draw mode.
 */
class ColorPickerDialog : DialogFragment() {

    private val colors by lazy { fetchPredefinedColors() }
    private val adapter by lazy { ColorAdapter(::colorPicked) }
    lateinit var onColorPicked: (Int) -> Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        colorsList.layoutManager = GridLayoutManager(requireContext(), 4)
        colorsList.adapter = adapter

        adapter.setData(colors)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_colors, container, false)
    }

    private fun colorPicked(@ColorInt color: Int) {
        onColorPicked(color)

        dismissAllowingStateLoss()
    }

    private fun fetchPredefinedColors(): List<Int> {
        return listOf(
            R.color.colorPrimary,
            R.color.colorAccent,
            R.color.gallery_white_EBE,
            R.color.cloud_burst_blue_1C2,
            R.color.some_yellow,
            R.color.picker_blue,
            R.color.picker_green,
            R.color.picker_pink,
            R.color.picker_orange,
            R.color.picker_light_blue,
            R.color.picker_brown,
            R.color.picker_marine_blue
        ).map { ContextCompat.getColor(requireContext(), it) }
    }

    override fun onResume() {
        super.onResume()

        val params: WindowManager.LayoutParams = dialog?.window?.attributes ?: return
        params.width = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            400f,
            resources.displayMetrics
        ).toInt()

        params.height = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            200f,
            resources.displayMetrics
        ).toInt()
        dialog?.window?.attributes = params
    }
}