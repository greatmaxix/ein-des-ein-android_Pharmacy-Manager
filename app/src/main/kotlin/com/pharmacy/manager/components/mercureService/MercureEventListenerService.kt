package com.pharmacy.manager.components.mercureService

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
import com.pharmacy.manager.R
import com.pharmacy.manager.components.chat.model.message.MessageItem
import com.pharmacy.manager.components.chatList.model.chat.ChatItem
import com.pharmacy.manager.components.main.MainActivity
import com.pharmacy.manager.components.mercureService.model.MercureResponse
import com.pharmacy.manager.components.mercureService.repository.MercureRepository
import com.pharmacy.manager.core.extensions.notificationManager
import com.pharmacy.manager.data.rest.response.SingleItemModel
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
                            val header = MessageItem.getHeaderInstance(this)
                            if (!repository.isHeaderExist(chatId, header.createdAt)) repository.insertMessageWithKey(header)
                        }
                        repository.insertMessageWithKey(this)

                        if (repository.getUserUuid() != ownerUuid && (!repository.isChatForeground || !isAppForeground)) {
                            val intent = getActionIntent().apply { putExtra(EXTRA_CHAT_ID, chatId) }
                            val notification = notification(
                                this@MercureEventListenerService,
                                MERCURE_NOTIFICATION_CHANNEL_ID,
                                smallIcon = R.drawable.ic_logo_stub
                            ) {
                                // TODO get chat from db else get chat from server and get user name from it
                                contentTitle("Message")
                                contentText(text ?: getString(if (file != null) R.string.attachment_image else R.string.attachment_product))
                                priority(NotificationCompat.PRIORITY_HIGH)
                                contentIntent(PendingIntent.getActivity(applicationContext, chatId, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                                autoCancel(true)
                            }

                            notificationManager.notify(chatId, notification)
                        }
                    }
                }
                MESSAGE_TYPE_CHANGE_STATUS -> {
                    val typeToken: TypeToken<SingleItemModel<ChatItem>> = object : TypeToken<SingleItemModel<ChatItem>>() {}
                    val item: SingleItemModel<ChatItem> = gson.fromJson(response.body, typeToken.type)
                    // TODO temp
                    if (item.item.status == ChatItem.STATUS_OPENED) {
                        repository.insertChatWithKeys(item.item)
                        repository.sendMessage(item.item.id, "Привет. Меня зовут Фармацевт")
                        repository.sendMessage(item.item.id, "Чем я могу вам помочь?")
                    }
                    // TODO add state of closed chat
                }
                else -> Timber.e("Unknown message type: ${response.type} >>> ${response.body}")
            }
        }
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
        isRunning = false
        sse?.close()
        coroutineJob.cancel()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
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

        private const val SERVICE_BASE_URL = "https:/mercure.pharmacies.fmc-dev.com/.well-known/mercure"
        private val MERCURE_NOTIFICATION_CHANNEL_ID = UUID.randomUUID().toString()
        private const val MERCURE_NOTIFICATION_CHANNEL_NAME = "Chat notification channel" // TODO set proper value

        private const val MESSAGE_TYPE_MESSAGE = "message"
        private const val MESSAGE_TYPE_APPLICATION = "application"
        private const val MESSAGE_TYPE_PRODUCT = "global_product"
        private const val MESSAGE_TYPE_CHANGE_STATUS = "change_status"

        const val EXTRA_CHAT_ID = "CHAT_ID"
    }
}