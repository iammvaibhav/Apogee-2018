package org.dvm.bits_apogee.ui.events

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.left_drawer.*
import kotlinx.android.synthetic.main.main_screen_main_content.*
import kotlinx.android.synthetic.main.right_drawer.*
import org.dvm.bits_apogee.R
import org.dvm.bits_apogee.data.DataManager
import org.dvm.bits_apogee.data.firebase.model.Event
import org.dvm.bits_apogee.data.firebase.model.FilterEvents
import org.dvm.bits_apogee.data.firebase.model.ShowBy
import org.dvm.bits_apogee.ui.base.BaseFragment
import org.dvm.bits_apogee.ui.main.CC
import org.dvm.bits_apogee.ui.main.MainActivity

/**
 * Created by Vaibhav on 04-02-2018.
 */

class EventsFragment : BaseFragment(), EventsMvpView, ViewPager.OnPageChangeListener, View.OnClickListener {

    private lateinit var viewPager: ViewPager
    private lateinit var next: ImageView
    private lateinit var prev: ImageView
    private lateinit var heading: TextView
    lateinit var eventClickListener: EventClickListener
    val evaluator = ArgbEvaluator()


    lateinit var regularFont: Typeface
    lateinit var lightFont: Typeface

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.events_fragment, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        next = view.findViewById(R.id.next)
        prev = view.findViewById(R.id.prev)
        next.setOnClickListener(this)
        prev.setOnClickListener(this)
        heading = view.findViewById(R.id.subLabel)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        regularFont = Typeface.createFromAsset(activity.assets, "fonts/regular.otf")
        lightFont = Typeface.createFromAsset(activity.assets, "fonts/light.otf")
        heading.typeface = regularFont
        eventClickListener = (activity as MainActivity)
        viewPager.addOnPageChangeListener(this)
        viewPager.addOnPageChangeListener((activity as MainActivity).bib)
        (activity as MainActivity).initialize()
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.next -> {
                val count = viewPager.childCount
                if (viewPager.currentItem -1 < count)
                    viewPager.setCurrentItem(viewPager.currentItem + 1, true)
            }
            R.id.prev -> {
                if (viewPager.currentItem > 0)
                    viewPager.setCurrentItem(viewPager.currentItem - 1, true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun setViewPagerAdapter(filterEvents: FilterEvents, data : List<Pair<String, List<Event>>>, dataManager: DataManager) {
        val showBy = if(filterEvents.showBy == ShowBy.DATE) 0 else 1
        viewPager.adapter = EventsViewPagerAdapter(data, showBy, dataManager, lightFont, regularFont, eventClickListener)
        if (data.isNotEmpty())
                heading.text = data[0].first
        else heading.text = "NO RESULTS"
        heading.setTextColor(CC.getScreenColorFor(0).colorB)
        next.setColorFilter(CC.getScreenColorFor(0).colorB)
        prev.setColorFilter(CC.getScreenColorFor(0).colorB)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                activity.window.statusBarColor = CC.getScreenColorFor(0).colorC
                (activity as MainActivity).currColor = CC.getScreenColorFor(0).colorC
                (activity as MainActivity).leftDrawer.setBackgroundColor(CC.getScreenColorFor(0).colorC)
                (activity as MainActivity).rightDrawer.setBackgroundColor(CC.getScreenColorFor(0).colorC)
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                activity.window.statusBarColor = evaluator.evaluate(positionOffset, CC.getScreenColorFor(position).colorC, CC.getScreenColorFor(position + 1).colorC) as Int
            }catch (e: Exception) {
                e.printStackTrace()
            }
            heading.setTextColor(evaluator.evaluate(positionOffset, CC.getScreenColorFor(position).colorB, CC.getScreenColorFor(position + 1).colorB) as Int)
            next.setColorFilter(evaluator.evaluate(positionOffset, CC.getScreenColorFor(position).colorB, CC.getScreenColorFor(position + 1).colorB) as Int)
            prev.setColorFilter(evaluator.evaluate(positionOffset, CC.getScreenColorFor(position).colorB, CC.getScreenColorFor(position + 1).colorB) as Int)
        }
    }

    override fun onPageSelected(position: Int) {
        try {
            (activity as MainActivity).currColor = CC.getScreenColorFor(position).colorC
            (activity as MainActivity).leftDrawer.setBackgroundColor(CC.getScreenColorFor(position).colorC)
            (activity as MainActivity).rightDrawer.setBackgroundColor(CC.getScreenColorFor(position).colorC)
        }catch (e: Exception) {
            e.printStackTrace()
        }
        val headText = (viewPager.adapter as EventsViewPagerAdapter?)?.queriedEvents?.get(position)?.first ?: ""
        heading.text = headText
        heading.setTextColor(CC.getScreenColorFor(position).colorB)
        next.setColorFilter(CC.getScreenColorFor(position).colorB)
        prev.setColorFilter(CC.getScreenColorFor(position).colorB)
    }
}