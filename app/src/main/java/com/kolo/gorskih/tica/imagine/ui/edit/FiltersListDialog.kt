package com.kolo.gorskih.tica.imagine.ui.edit

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kolo.gorskih.tica.imagine.R
import ja.burhanrashid52.photoeditor.PhotoFilter
import kotlinx.android.synthetic.main.dialog_filters.*


/**
 * TODO - Add comment
 */
class FiltersListDialog : DialogFragment() {

    lateinit var onFilterSelected: (PhotoFilter) -> Unit

    private val filtersAdapter by lazy { PhotoFilterAdapter(::onFilterItemSelected) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filtersList.layoutManager = LinearLayoutManager(context)
        filtersList.adapter = filtersAdapter

        filtersAdapter.setData(PhotoFilter.values().toList())
    }

    private fun onFilterItemSelected(photoFilter: PhotoFilter) {
        onFilterSelected(photoFilter)
        dismissAllowingStateLoss()
    }

    override fun onResume() {
        super.onResume()

        val params: WindowManager.LayoutParams = dialog?.window?.attributes ?: return
        params.width = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            300f,
            resources.displayMetrics
        ).toInt()

        params.height = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            500f,
            resources.displayMetrics
        ).toInt()
        dialog?.window?.attributes = params
    }
}