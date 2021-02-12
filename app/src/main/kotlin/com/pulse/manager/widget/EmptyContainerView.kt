package com.pulse.manager.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.widget.LinearLayout
import com.pulse.manager.R
import com.pulse.manager.core.extensions.inflater
import com.pulse.manager.core.extensions.setDebounceOnClickListener
import com.pulse.manager.core.extensions.use
import com.pulse.manager.core.extensions.visibleOrGone
import com.pulse.manager.databinding.LayoutEmptyContainerBinding

class EmptyContainerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutEmptyContainerBinding.inflate(inflater, this)
    private var title = ""
    private var subtitle = ""
    private var buttonText = ""
    private var isButtonVisible = true

    init {
        attrs?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.EmptyContainerView, defStyleAttr, -1)
                .use {
                    title = getString(R.styleable.EmptyContainerView_titleEmpty) ?: ""
                    subtitle = getString(R.styleable.EmptyContainerView_subtitleEmpty) ?: ""
                    buttonText = getString(R.styleable.EmptyContainerView_buttonTextEmpty) ?: ""
                    isButtonVisible = getBoolean(R.styleable.EmptyContainerView_isButtonVisible, true)
                }
        }
        gravity = CENTER
        orientation = VERTICAL
    }

    override fun onFinishInflate() = with(binding) {
        super.onFinishInflate()
        mtvTitle.text = title
        mtvSubtitle.text = subtitle

        if (isButtonVisible && buttonText.isNotEmpty()) {
            mbAction.text = buttonText
        }
        mbAction.visibleOrGone(isButtonVisible)
    }

    fun setButtonAction(action: () -> Unit) = binding.mbAction.setDebounceOnClickListener { action() }
}