package com.pulse.manager.core.general.behavior

import android.view.View
import androidx.core.view.isGone
import com.pulse.manager.core.extensions.animateGone
import com.pulse.manager.core.extensions.animateVisible

class ProgressViewBehavior(private var progress: View?) : IProgressBehavior {

    override fun showProgress() {
        progress?.apply {
            if (isGone) {
                animateVisible(50)
            }
        }
    }

    override fun hideProgress() {
        progress?.animateGone(50)
    }

    override fun detach() {
        progress = null
    }
}