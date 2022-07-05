package com.pulse.manager.components.notifications

import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.manager.components.notifications.model.NotificationState
import com.pulse.manager.components.notifications.repository.NotificationsRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class NotificationsViewModel(private val repository: NotificationsRepository) : BaseViewModel() {

    var notificationState = StateEventFlow(NotificationState(isPushEnabled = true, isEmailEnabled = true, isChatRequestEnabled = true, isRatingUpdateEnabled = false))

    fun changePushState(checked: Boolean) {
        notificationState.postState(notificationState.value.copy(isPushEnabled = checked))
    }

    fun changeEmailState(checked: Boolean) {
        notificationState.postState(notificationState.value.copy(isEmailEnabled = checked))
    }

    fun changeChatRequestState(checked: Boolean) {
        notificationState.postState(notificationState.value.copy(isChatRequestEnabled = checked))
    }

    fun changeRatingUpdateState(checked: Boolean) {
        notificationState.postState(notificationState.value.copy(isRatingUpdateEnabled = checked))
    }
}