package com.pandulapeter.beagle.core.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.listeners.UpdateListener
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.dimension
import com.pandulapeter.beagle.utils.extensions.drawable
import com.pandulapeter.beagle.utils.view.GestureBlockingRecyclerView

class InternalDebugMenuView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context.applyTheme(), attrs, defStyleAttr), UpdateListener {

    private val verticalMargin = context.dimension(R.dimen.beagle_item_vertical_margin)
    private var recyclerLeftPadding = 0
    private var recyclerTopPadding = verticalMargin
    private var recyclerRightPadding = 0
    private var recyclerBottomPadding = verticalMargin

    //TODO: Create a custom view for the button container and all related logic and improve their appearance
    private val largePadding = context.dimension(R.dimen.beagle_large_content_padding)
    private val applyButton = MaterialButton(context.applyTheme(), attrs, R.attr.materialButtonStyle).apply {
        isAllCaps = false
        setText(BeagleCore.implementation.appearance.generalTexts.applyButtonText)
        setPadding(largePadding, largePadding, largePadding, largePadding)
        setOnClickListener { BeagleCore.implementation.applyPendingChanges() }
    }
    private val resetButton = MaterialButton(context.applyTheme(), attrs, R.attr.materialButtonStyle).apply {
        isAllCaps = false
        setText(BeagleCore.implementation.appearance.generalTexts.resetButtonText)
        setPadding(largePadding, largePadding, largePadding, largePadding)
        setOnClickListener { BeagleCore.implementation.resetPendingChanges() }
    }
    private val buttonContainer = LinearLayout(context.applyTheme(), attrs, defStyleAttr).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
        clipChildren = false
        visibility = View.INVISIBLE
        addView(applyButton, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            leftMargin = largePadding
            rightMargin = largePadding / 2
            marginStart = leftMargin
            marginEnd = rightMargin
        })
        addView(resetButton, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            leftMargin = largePadding / 2
            rightMargin = largePadding
            marginStart = leftMargin
            marginEnd = rightMargin
        })
        background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(Color.TRANSPARENT, context.applyTheme().colorResource(android.R.attr.textColorPrimary)))
    }
    private val recyclerView = GestureBlockingRecyclerView(context.applyTheme(), attrs, defStyleAttr).apply {
        clipToPadding = false
        minimumWidth = context.dimension(R.dimen.beagle_minimum_size)
        minimumHeight = context.dimension(R.dimen.beagle_minimum_size)
    }

    init {
        setBackgroundFromWindowBackground()
        addView(recyclerView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(buttonContainer, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply { gravity = Gravity.BOTTOM })
        onContentsChanged()
        applyInsets(0, 0, 0, 0)
    }

    @CallSuper
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        BeagleCore.implementation.addInternalUpdateListener(this)
        BeagleCore.implementation.setupRecyclerView(recyclerView)
        post { recyclerView.updatePadding(BeagleCore.implementation.hasPendingUpdates) }
    }

    @CallSuper
    override fun onDetachedFromWindow() {
        BeagleCore.implementation.removeUpdateListener(this)
        recyclerView.adapter = null
        super.onDetachedFromWindow()
    }

    override fun onContentsChanged() {
        val hasPendingChanges = try {
            BeagleCore.implementation.hasPendingUpdates
        } catch (_: ConcurrentModificationException) {
            false
        }
        post {
            recyclerView.updatePadding(hasPendingChanges)
            buttonContainer.run {
                if (hasPendingChanges) {
                    visibility = View.VISIBLE
                }
                post {
                    animate()
                        .alpha(if (hasPendingChanges) 1f else 0f)
                        .start()
                    applyButton.animate().translationY(if (hasPendingChanges) 0f else height.toFloat()).start()
                    resetButton.animate().translationY(if (hasPendingChanges) 0f else height.toFloat()).start()
                }
            }
        }
    }

    fun applyInsets(left: Int, top: Int, right: Int, bottom: Int) {
        post {
            val scrollBy = recyclerTopPadding - top - verticalMargin
            recyclerLeftPadding = left
            recyclerTopPadding = top + verticalMargin
            recyclerRightPadding = right
            recyclerBottomPadding = bottom + verticalMargin
            buttonContainer.setPadding(left, top, right, bottom + largePadding)
            recyclerView.updatePadding(BeagleCore.implementation.hasPendingUpdates)
            recyclerView.scrollBy(0, scrollBy)
        }
    }

    private fun setBackgroundFromWindowBackground() {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            setBackgroundColor(typedValue.data)
        } else {
            background = context.drawable(typedValue.resourceId)
        }
    }

    private fun RecyclerView.updatePadding(hasPendingUpdates: Boolean) = setPadding(
        recyclerLeftPadding,
        recyclerTopPadding,
        recyclerRightPadding,
        if (hasPendingUpdates) verticalMargin + buttonContainer.height else recyclerBottomPadding
    )
}