package com.bbbdem.koha.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import com.bbbdem.koha.R

@SuppressLint("CustomViewStyleable")
open class TrackedConstraintLayout(context: Context?, attrs: AttributeSet?, defStyle: Int) :
    ConstraintLayout(
        context!!,
        attrs,
        defStyle
    ) {

    private var trackingName: String = javaClass.simpleName

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {
        var typedArray: TypedArray? = null
        try {
            trackingName = resources.getResourceEntryName(id)
            typedArray = context?.obtainStyledAttributes(attrs, R.styleable.Tracking, 0, 0)
            val customName = typedArray?.getString(R.styleable.Tracking_trackingName)
            if (!customName.isNullOrEmpty()) {
                // we've set a custom name to this view, so use that instead of id.
                trackingName = customName
            }
        } catch (e: Exception) {
            // no id - ignore
        } finally {
            typedArray?.recycle()
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            // full click event
            //trackView(this, trackingName, "Control")
        }
        return super.onTouchEvent(event)
    }
}