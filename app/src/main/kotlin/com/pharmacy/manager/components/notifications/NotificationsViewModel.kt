package com.pharmacy.manager.components.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pharmacy.manager.components.notifications.model.NotificationState
import com.pharmacy.manager.components.notifications.repository.NotificationsRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel

class NotificationsViewModel(private val repository: NotificationsRepository) : BaseViewModel() {

    private var notificationState: NotificationState =
        NotificationState(isPushEnabled = true, isEmailEnabled = true, isChatRequestEnabled = true, isRatingUpdateEnabled = false) // TODO get from repo
    private val _notificationStateLiveData by lazy { MutableLiveData<NotificationState>() }
    val notificationStateLiveData: LiveData<NotificationState> by lazy { _notificationStateLiveData }

    fun changePushState(checked: Boolean) {
        notificationState = notificationState.copy(isPushEnabled = checked)
    }

    fun changeEmailState(checked: Boolean) {
        notificationState = notificationState.copy(isEmailEnabled = checked)
    }

    fun changeChatRequestState(checked: Boolean) {
        notificationState = notificationState.copy(isChatRequestEnabled = checked)
    }

    fun changeRatingUpdateState(checked: Boolean) {
        notificationState = notificationState.copy(isRatingUpdateEnabled = checked)
    }

    fun getNotificationState() {
        _notificationStateLiveData.value = notificationState
    }
}