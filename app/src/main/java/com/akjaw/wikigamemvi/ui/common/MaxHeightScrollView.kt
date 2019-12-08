package com.akjaw.wikigamemvi.ui.common

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView
import android.R.attr.maxHeight
import com.akjaw.wikigamemvi.R


class MaxHeightScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    private val DEFAULT_HEIGHT = context.resources
        .getDimension(R.dimen.default_scroll_view_height_cap).toInt()
    private var maxHeight: Int = DEFAULT_HEIGHT


    init {
        if (attrs != null) {
            val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView)
            maxHeight = styledAttrs.getDimensionPixelSize(
                R.styleable.MaxHeightScrollView_maxHeight,
                DEFAULT_HEIGHT
            )

            styledAttrs.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }
}