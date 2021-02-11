package com.pulse.manager.widget

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.pulse.manager.R
import com.pulse.manager.core.extensions.*
import com.pulse.manager.databinding.LayoutProfileItemBinding

class ProfileItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutProfileItemBinding.inflate(inflater, this, true)
    var icon: Int = -1
        set(value) {
            field = value
            if (value != -1) binding.ivIcon.setImageResource(icon)
            else binding.ivIcon.setImageDrawable(null)
        }
    var title: String = ""
        set(value) {
            field = value
            binding.mtvTitle.text = value
        }
    var arrowVisibility: Boolean = true
        set(value) {
            field = value
            binding.ivArrow.visibleOrGone(value)
        }
    var mainColor: Int = -1
        set(value) {
            field = value
            binding.ivIcon.setColorFilter(ContextCompat.getColor(context, value), PorterDuff.Mode.SRC_IN)
            binding.mtvTitle.textColor(value)
        }
    var secondaryColor: Int = -1
        set(value) {
            field = value
            binding.ivIcon.backgroundTintList = ContextCompat.getColorStateList(context, value)
        }
    val selectColor: Int = R.color.primaryBlue

    init {
        attrs?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.ProfileItemView, defStyleAttr, -1)
                .use {
                    icon = getResourceId(R.styleable.ProfileItemView_iconProfile, -1)
                    title = getString(R.styleable.ProfileItemView_titleProfile) ?: ""
                    arrowVisibility = getBoolean(R.styleable.ProfileItemView_arrowVisibilityProfile, true)
                    mainColor = getResourceId(R.styleable.ProfileItemView_mainColorProfile, R.color.darkBlue)
                    secondaryColor = getResourceId(R.styleable.ProfileItemView_secondaryColorProfile, R.color.profileIconBackground)
                }
        }
    }

    fun setOnClick(f: View.() -> Unit) = binding.mcvProfileContainer.setDebounceOnClickListener(listener = f)

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)

        with(binding) {
            ivArrow.rotation = if (selected) -90f else 0f
            ivArrow.setColorFilter(context.compatColor(if (selected) selectColor else mainColor), PorterDuff.Mode.SRC_IN)
            ivIcon.setColorFilter(context.compatColor(if (selected) selectColor else mainColor), PorterDuff.Mode.SRC_IN)
            mtvTitle.textColor(if (selected) selectColor else mainColor)
        }
    }
}