package bitspilani.dvm.apogee2016.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import bitspilani.dvm.apogee2016.data.prefs.AppPreferencesHelper
import bitspilani.dvm.apogee2016.ui.informatives.NotificationData
import bitspilani.dvm.apogee2016.ui.splash.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onMessageReceived(message: RemoteMessage) {

        val title = message.data["title"] ?: ""
        val body = message.data["body"] ?: ""

        val date = Date(message.sentTime)
        val receive = "Sent on ${SimpleDateFormat("dd MMM").format(date)} at ${SimpleDateFormat("HH:mm").format(date)}"

        sendNotification(title, body)
        AppPreferencesHelper(applicationContext).putNotification(NotificationData(title, body, receive, date))
    }

    private fun sendNotification(title: String, body: String){
        val notificationBuilder = NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSmallIcon(android.R.drawable.stat_notify_more) //TODO(Change this icon)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(body))

        val intent = Intent(this, SplashActivity::class.java)
        intent.putExtra("calledBy", "notification")
        val id = Integer.parseInt(System.currentTimeMillis().toString().reversed().substring(0, 9))
        intent.action = "$id"
        val activity = PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_ONE_SHOT)
        notificationBuilder.setContentIntent(activity)

        val notification = notificationBuilder.build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, notification)
    }
}