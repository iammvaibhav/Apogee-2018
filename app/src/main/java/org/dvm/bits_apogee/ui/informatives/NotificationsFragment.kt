package org.dvm.bits_apogee.ui.informatives

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.dvm.bits_apogee.R
import org.dvm.bits_apogee.data.DataManager
import org.dvm.bits_apogee.databinding.NotificationsItemBinding
import org.dvm.bits_apogee.di.SemiBold
import org.dvm.bits_apogee.ui.base.BaseFragment
import org.dvm.bits_apogee.ui.main.MainActivity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class NotificationData(val title: String, val body: String, val timeReceived: String, val date: Date) {

    override fun toString(): String {
        return "$title!`$body!`$timeReceived!`${format.format(date)}"
    }

    companion object {
        val format = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        fun fromString(notificationData: String): NotificationData {
            val x = notificationData.split("!`")
            return NotificationData(x[0], x[1], x[2], format.parse(x[3]))
        }
    }
}

class NotificationViewHolder(val binding: NotificationsItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(data: NotificationData, typeface: Typeface){
        binding.data = data
        binding.typeface = typeface
    }
}

class NotificationsAdapter(val typeface: Typeface, val notifications: List<NotificationData>) : RecyclerView.Adapter<NotificationViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(NotificationsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = notifications.size

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position], typeface)
    }
}

class NotificationsFragment : BaseFragment(){

    @Inject
    @field:SemiBold
    lateinit var typeface: Typeface

    @Inject
    lateinit var dataManager: DataManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getFragmentComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        (getBaseActivity() as MainActivity).setHeading("Notifications")
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val notifications = dataManager.getNotifications()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = NotificationsAdapter(typeface, notifications)
        return view
    }
}
