package org.dvm.bits_apogee.ui.events

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sackcentury.shinebuttonlib.ShineButton
import org.dvm.bits_apogee.R
import org.dvm.bits_apogee.data.DataManager
import org.dvm.bits_apogee.data.firebase.model.Event
import org.dvm.bits_apogee.notification.NotificationPublisher
import org.dvm.bits_apogee.ui.main.CC
import org.dvm.bits_apogee.ui.splash.SplashActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Vaibhav on 06-02-2018.
 */

class EventsRecylerViewAdapter(val eventsData: List<Event>, val showBy: Int, val dataManager: DataManager,
                               val lightFont: Typeface, val regularFont: Typeface, val position: Int,
                               val eventClickListener: EventClickListener) : RecyclerView.Adapter<EventsRecylerViewAdapter.Companion.EventsRecyclerViewViewHolder>(){

    companion object {
        class EventsRecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val eventName = itemView.findViewById<TextView>(R.id.eventName)
            val eventVenue = itemView.findViewById<TextView>(R.id.venue)
            val eventTime = itemView.findViewById<TextView>(R.id.time)
            val markFavourite = itemView.findViewById<ShineButton>(R.id.mark_favourite)
        }
    }

    val parse = SimpleDateFormat("HH:mm")
    val shows0 = SimpleDateFormat("hh:mm a")
    val shows1 = SimpleDateFormat("MMM d, hh:mm a")

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EventsRecyclerViewViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.event_item, parent, false)
        val holder = EventsRecyclerViewViewHolder(itemView)
        holder.eventName.typeface = regularFont
        holder.eventTime.typeface = lightFont
        holder.eventVenue.typeface = lightFont

        return holder
    }

    override fun onBindViewHolder(holder: EventsRecyclerViewViewHolder, position: Int) {
        // showBy == 0 -> by date & 1 -> by category

        holder.markFavourite.setBtnFillColor(CC.getScreenColorFor(this.position).colorB)
        holder.markFavourite.setAllowRandomColor(true)
        val event = eventsData[position]

        //setting basic data
        holder.eventName.text = event.name
        holder.eventVenue.text = event.venue

        Log.e("sdf", parse.parse(event.startTime).toString())

        try {
            if (showBy == 0) {
                holder.eventTime.text = shows0.format(parse.parse(event.startTime))
            } else {
                holder.eventTime.text = "Feb ${event.day}, ${shows0.format(parse.parse(event.startTime))}"
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }

        //decide if to show as favourite or not
        holder.markFavourite.isChecked = dataManager.isFavourite(event.id)

        holder.markFavourite.setOnCheckStateChangeListener { view, checked ->
            if (checked) {
                dataManager.addAsFavourite(event.id)
                scheduleNotification(holder.itemView.context, event)
            }
            else {
                dataManager.removeAsFavourite(event.id)
                cancelNotification(holder.itemView.context, event)
            }
        }

        holder.itemView.setOnClickListener {
            val shows2 = SimpleDateFormat("hh:mm a")
            var tim = ""
            try {
                tim = shows2.format(parse.parse(event.startTime)) + ", DAY ${event.day - 21}"
            }catch (e: Exception) {
                e.printStackTrace()
            }
            eventClickListener.onEventClick(event, dataManager.isFavourite(event.id), tim)
        }
    }

    override fun getItemCount() = eventsData.size

    private fun scheduleNotification(context: Context, event: Event){
        val data = getPendingIntentAndTime(context, event)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, data.second, data.first)
    }

    private fun cancelNotification(context: Context, event: Event){
        val data = getPendingIntentAndTime(context, event)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(data.first)
    }

    private fun getPendingIntentAndTime(context: Context, event: Event): Pair<PendingIntent, Long>{
        val contentTitle = "Event Reminder"
        val shortContentText = "${event.name} is gonna start in 30 min from now."
        val longContentText = "${event.name} is gonna start in 30 min from now."

        val inFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
        var date = Date()
        try {
            date = inFormat.parse("${event.day}-02-2018 ${event.startTime}")
        }catch (e: ParseException){
            e.printStackTrace()
        }

        val notificationTime = date.time - 30 * 60 * 1000
        val currTime = System.currentTimeMillis()
        val delay = notificationTime - currTime

        val notificationBuilder = NotificationCompat.Builder(context)
                .setContentTitle(contentTitle)
                .setContentText(shortContentText)
                .setAutoCancel(true)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(longContentText))

        val intent = Intent(context, SplashActivity::class.java)
        intent.action = "${event.id}"
        intent.putExtra("event", event)
        val activity = PendingIntent.getActivity(context, event.id, intent, PendingIntent.FLAG_ONE_SHOT)
        notificationBuilder.setContentIntent(activity)

        val notification = notificationBuilder.build()

        val notificationIntent = Intent(context, NotificationPublisher::class.java)
        notificationIntent.action = "${event.id}"
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, event.id)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(context, event.id, notificationIntent, PendingIntent.FLAG_ONE_SHOT)

        val futureInMillis = SystemClock.elapsedRealtime() + delay

        return Pair(pendingIntent, futureInMillis)
    }
}