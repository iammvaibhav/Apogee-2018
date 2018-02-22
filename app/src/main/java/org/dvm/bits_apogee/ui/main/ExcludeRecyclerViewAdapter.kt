package org.dvm.bits_apogee.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.dvm.bits_apogee.R
import org.dvm.bits_apogee.data.firebase.model.FilterEvents
import org.dvm.bits_apogee.ui.SmoothCheckBox.SmoothCheckBox

/**
 * Created by Vaibhav on 11-02-2018.
 */

class ExcludeRecyclerViewAdapter(val list: List<String>, val filterEvents: FilterEvents, val select: Int) : RecyclerView.Adapter<ExcludeRecyclerViewAdapter.Companion.ExcludeRecyclerViewVH>() {
    companion object {
        class ExcludeRecyclerViewVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val name = itemView.findViewById<TextView>(R.id.name)
            val check = itemView.findViewById<SmoothCheckBox>(R.id.checkbox)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ExcludeRecyclerViewVH {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.exclude_item, parent, false)
        return ExcludeRecyclerViewVH(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ExcludeRecyclerViewVH, position: Int) {
        holder.name.text = list[position]
        when(select) {
            0 -> holder.check.isChecked = list[position] !in filterEvents.excludeVenue
            1 -> holder.check.isChecked = list[position] !in filterEvents.excludeCategory
        }

        
        holder.check.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked)
                when(select) {
                    0 -> filterEvents.excludeVenue.remove(list[position])
                    1 -> filterEvents.excludeCategory.remove(list[position])
                }
            else
                when(select) {
                    0 -> filterEvents.excludeVenue.add(list[position])
                    1 -> filterEvents.excludeCategory.add(list[position])
                }
        }
    }
}