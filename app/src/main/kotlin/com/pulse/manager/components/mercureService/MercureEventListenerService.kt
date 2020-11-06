package com.pulse.manager.components.mercureService

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.here.oksse.OkSse
import com.here.oksse.ServerSentEvent
import com.kirich1409.androidnotificationdsl.channels.createNotificationChannels
import com.kirich1409.androidnotificationdsl.notification
import com.pulse.manager.R
import com.pulse.manager.components.chat.adapter.ChatMessageAdapter.Companion.TYPE_DATE_HEADER
import com.pulse.manager.components.chat.adapter.ChatMessageAdapter.Companion.TYPE_END_CHAT
import com.pulse.manager.components.chat.model.message.MessageItem
import com.pulse.manager.components.chatList.model.chat.ChatItem
import com.pulse.manager.components.main.MainActivity
import com.pulse.manager.components.mercureService.model.MercureResponse
import com.pulse.manager.components.mercureService.repository.MercureRepository
import com.pulse.manager.core.extensions.notificationManager
import com.pulse.manager.data.rest.response.SingleItemModel
import kotlinx.coroutines.*
import okhttp3.Request
import okhttp3.Response
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinApiExtension
import timber.log.Timber
import java.util.*
import kotlin.coroutines.CoroutineContext

@KoinApiExtension
class MercureEventListenerService : Service(), CoroutineScope, LifecycleObserver {

    private var coroutineJob: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob
    private var isRunning = false
    private var isAppForeground = false
    private lateinit var request: Request
    private val repository: MercureRepository by inject()
    private val okSse: OkSse by inject()
    private val gson: Gson by inject()
    private var sse: ServerSentEvent? = null
    private val sseListener by lazy {
        object : ServerSentEvent.Listener {
            override fun onOpen(sse: ServerSentEvent?, response: Response?) {
                Timber.i("SSE connection opened")
            }

            override fun onMessage(sse: ServerSentEvent?, id: String?, event: String?, message: String?) {
                parseMessage(message)
            }

            override fun onComment(sse: ServerSentEvent?, comment: String?) {
                // no op
            }

            override fun onRetryTime(sse: ServerSentEvent?, milliseconds: Long): Boolean = true

            override fun onRetryError(sse: ServerSentEvent?, throwable: Throwable?, response: Response?): Boolean = true

            override fun onClosed(sse: ServerSentEvent?) {
                Timber.i("SSE connection closed")
            }

            override fun onPreRetry(sse: ServerSentEvent?, originalRequest: Request?): Request = request
        }
    }

    private fun parseMessage(jsonString: String?) {
        launch {
            Timber.d("SSE json received: $jsonString")
            val response = gson.fromJson(jsonString, MercureResponse::class.java)
            when (response.type) {
                MESSAGE_TYPE_MESSAGE, MESSAGE_TYPE_APPLICATION, MESSAGE_TYPE_PRODUCT -> {
                    val typeToken: TypeToken<SingleItemModel<MessageItem>> = object : TypeToken<SingleItemModel<MessageItem>>() {}
                    val messageItem: SingleItemModel<MessageItem> = gson.fromJson(response.body, typeToken.type)
                    with(messageItem.item) {
                        val lastMessage = repository.getLastMessage(chatId)
                        if (lastMessage == null || lastMessage.createdAt.dayOfMonth != createdAt.dayOfMonth) {
                            val header = MessageItem.getStubItem(null, this, TYPE_DATE_HEADER, chatId)
                            if (!repository.isHeaderExist(chatId, header.createdAt)) repository.insertMessageWithKey(header)
                        }
                        repository.insertMessageWithKey(this)
                        if (repository.getUserUuid() != ownerUuid && (!repository.isChatForeground || !isAppForeground)) postNotification()
                    }
                }
                MESSAGE_TYPE_CHANGE_STATUS -> {
                    val typeToken: TypeToken<SingleItemModel<ChatItem>> = object : TypeToken<SingleItemModel<ChatItem>>() {}
                    val item: SingleItemModel<ChatItem> = gson.fromJson(response.body, typeToken.type)
                    with(item.item) {
                        repository.insertChatWithKeys(this)

                        if (status == ChatItem.STATUS_CLOSE_REQUEST) {
                            val lastMessage = repository.getLastMessage(id)
                            repository.insertMessageWithKey(MessageItem.getStubItem(null, lastMessage, TYPE_END_CHAT, id))
                        } else {
                            repository.clearEndChatMessage(id)
                        }
                    }
                }
                else -> Timber.e("Unknown message type: ${response.type} >>> ${response.body}")
            }
        }
    }

    private suspend fun MessageItem.postNotification() {
        val intent = getActionIntent().apply { putExtra(EXTRA_CHAT_ID, chatId) }
        val notification = notification(
            this@MercureEventListenerService,
            MERCURE_NOTIFICATION_CHANNEL_ID,
            smallIcon = R.drawable.ic_logo
        ) {
            val chat = repository.getChat(chatId)
            chat?.let { contentTitle(it.customer.name) }
            contentText(text ?: getString(if (file != null) R.string.attachment_image else R.string.attachment_product))
            priority(NotificationCompat.PRIORITY_HIGH)
            contentIntent(PendingIntent.getActivity(applicationContext, chatId, intent, PendingIntent.FLAG_UPDATE_CURRENT))
            autoCancel(true)
        }

        notificationManager.notify(chatId, notification)
    }

    private fun getActionIntent(): Intent {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return intent
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRunning) {
            isRunning = true
            launch(Dispatchers.IO) {
                val topicName = repository.getTopicName()
                val path = "$SERVICE_BASE_URL?topic=$topicName"
                request = Request.Builder()
                    .url(path)
                    .build()
                sse = okSse.newServerSentEvent(request, sseListener)
            }

            initNotificationsChannel()
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        }
        return START_STICKY
    }

    private fun initNotificationsChannel() {
        createNotificationChannels(this) {
            channel(MERCURE_NOTIFICATION_CHANNEL_ID, MERCURE_NOTIFICATION_CHANNEL_NAME)
        }
    }

    override fun onDestroy() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        isRunning = false
        sse?.close()
        coroutineJob.cancel()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        isAppForeground = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        isAppForeground = true
    }

    companion object {

        // DEV https:/mercure.pharmacies.fmc-dev.com/ RELEASE https://mercure.pharmacies.release.fmc-dev.com/
        private const val SERVICE_BASE_URL = "https://mercure.pharmacies.release.fmc-dev.com/.well-known/mercure"
        private val MERCURE_NOTIFICATION_CHANNEL_ID = UUID.randomUUID().toString()
        private const val MERCURE_NOTIFICATION_CHANNEL_NAME = "Chat notification channel" // TODO set proper value

        private const val MESSAGE_TYPE_MESSAGE = "message"
        private const val MESSAGE_TYPE_APPLICATION = "application"
        private const val MESSAGE_TYPE_PRODUCT = "global_product"
        private const val MESSAGE_TYPE_CHANGE_STATUS = "change_status"

        const val EXTRA_CHAT_ID = "CHAT_ID"
    }
}