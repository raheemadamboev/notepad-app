package xyz.teamgravity.notepad.core.notification

import android.app.Application
import android.app.Notification
import xyz.teamgravity.coresdkandroid.notification.NotificationManager
import xyz.teamgravity.notepad.R

class TrashNotification(
    private val application: Application,
    private val manager: NotificationManager
) {

    private companion object {
        const val CHANNEL_ID = "xyz.teamgravity.notepad.TrashNotification"
        const val NOTIFICATION_ID = 7000
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun notification(): Notification {
        manager.createChannel(
            id = CHANNEL_ID,
            name = R.string.trash_notification_channel_name,
            description = R.string.trash_notification_channel_description
        )
        return manager.createNotification(
            channelId = CHANNEL_ID,
            smallIcon = R.drawable.icon_notification_small,
            largeIcon = R.drawable.icon_notification_large,
            message = application.getString(R.string.trash_notification_message),
            autoCancel = false
        )
    }

    fun id(): Int {
        return NOTIFICATION_ID
    }
}