package com.pulse.manager.components.notifications

import android.os.Bundle
import android.view.View
import com.pulse.manager.R
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import kotlinx.android.synthetic.main.fragment_notifications.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class NotificationsFragment(private val vm: NotificationsViewModel) : BaseMVVMFragment(R.layout.fragment_notifications) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        vm.getNotificationState()

        switchPushNotifications.setOnCheckedChangeListener { _, isChecked -> vm.changePushState(isChecked) }
        switchEmailNotifications.setOnCheckedChangeListener { _, isChecked -> vm.changeEmailState(isChecked) }
        switchNewChatRequestNotifications.setOnCheckedChangeListener { _, isChecked -> vm.changeChatRequestState(isChecked) }
        switchRatingUpdateNotifications.setOnCheckedChangeListener { _, isChecked -> vm.changeRatingUpdateState(isChecked) }
    }

    override fun onBindLiveData() {
        observe(vm.notificationStateLiveData) {
            switchPushNotifications.isChecked = isPushEnabled
            switchEmailNotifications.isChecked = isEmailEnabled
            switchNewChatRequestNotifications.isChecked = isChatRequestEnabled
            switchRatingUpdateNotifications.isChecked = isRatingUpdateEnabled
        }
    }
}