package com.pulse.manager.components.notifications.model

data class NotificationState(
    val isPushEnabled: Boolean,
    val isEmailEnabled: Boolean,
    val isChatRequestEnabled: Boolean,
    val isRatingUpdateEnabled: Boolean
)