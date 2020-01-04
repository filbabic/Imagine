package com.kolo.gorskih.tica.imagine.ui.edit

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.kolo.gorskih.tica.imagine.R
import ja.burhanrashid52.photoeditor.PhotoEditor
import kotlinx.android.synthetic.main.dialog_emoji.*

/**
 * Shows a list of emoji to pick, to add to the image.
 */
class EmojiListDialog : DialogFragment() {

    lateinit var onEmojiSelected: (String) -> Unit

    private val adapter by lazy { EmojiAdapter(::emojiSelected) }
    private val emojis by lazy { PhotoEditor.getEmojis(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_emoji, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emojiList.layoutManager = GridLayoutManager(requireContext(), 5)
        emojiList.adapter = adapter

        adapter.setData(emojis)
    }

    private fun emojiSelected(emoji: String) {
        onEmojiSelected(emoji)

        dismissAllowingStateLoss()
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
            500f,
            resources.displayMetrics
        ).toInt()
        dialog?.window?.attributes = params
    }
}