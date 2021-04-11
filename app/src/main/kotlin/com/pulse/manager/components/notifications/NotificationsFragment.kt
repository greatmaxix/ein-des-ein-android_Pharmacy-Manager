package com.pulse.manager.components.notifications

import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.core.base.fragment.BaseToolbarFragment
import com.pulse.manager.core.extensions.observe
import com.pulse.manager.databinding.FragmentNotificationsBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class NotificationsFragment : BaseToolbarFragment<NotificationsViewModel>(R.layout.fragment_notifications, NotificationsViewModel::class) {

    private val binding by viewBinding(FragmentNotificationsBinding::bind)

    override fun initUI()  = with(binding) {
        showBackButton()
        switchPushNotifications.setOnCheckedChangeListener { _, isChecked -> viewModel.changePushState(isChecked) }
        switchEmailNotifications.setOnCheckedChangeListener { _, isChecked -> viewModel.changeEmailState(isChecked) }
        switchNewChatRequestNotifications.setOnCheckedChangeListener { _, isChecked -> viewModel.changeChatRequestState(isChecked) }
        switchRatingUpdateNotifications.setOnCheckedChangeListener { _, isChecked -> viewModel.changeRatingUpdateState(isChecked) }
    }

    override fun onBindStates() = with(lifecycleScope) {
        with(binding) {
            observe(viewModel.notificationState) {
                switchPushNotifications.isChecked = it.isPushEnabled
                switchEmailNotifications.isChecked = it.isEmailEnabled
                switchNewChatRequestNotifications.isChecked = it.isChatRequestEnabled
                switchRatingUpdateNotifications.isChecked = it.isRatingUpdateEnabled
            }
        }
    }
}