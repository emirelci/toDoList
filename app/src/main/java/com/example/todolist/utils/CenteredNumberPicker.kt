package com.example.todolist.utils

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.NumberPicker

class CenteredNumberPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NumberPicker(context, attrs, defStyleAttr) {

    init {
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun addView(child: android.view.View?) {
        super.addView(child)
        updateView()
    }

    override fun addView(child: android.view.View?, params: android.view.ViewGroup.LayoutParams?) {
        super.addView(child, params)
        updateView()
    }

    override fun addView(child: android.view.View?, index: Int, params: android.view.ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        updateView()
    }

    private fun updateView() {
        // NumberPicker'ın alt ve üst görünümlerini gizle
        descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
    }
}