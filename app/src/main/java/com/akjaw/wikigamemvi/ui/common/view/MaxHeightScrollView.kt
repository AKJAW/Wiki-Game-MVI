package com.akjaw.wikigamemvi.ui.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView
import android.R.attr.maxHeight
import com.akjaw.wikigamemvi.R


class MaxHeightScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    private val defaultHeight = context.resources
        .getDimension(R.dimen.default_scroll_view_height_cap).toInt()
    private val maxHeight: Int


    init {
        if (attrs != null) {
            val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView)
            maxHeight =  styledAttrs.getDimensionPixelSize(
                R.styleable.MaxHeightScrollView_maxHeight,
                defaultHeight
            )

            styledAttrs.recycle()
        } else {
            maxHeight = defaultHeight
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }
}