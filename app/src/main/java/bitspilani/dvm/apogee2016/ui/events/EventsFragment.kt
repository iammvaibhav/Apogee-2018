package bitspilani.dvm.apogee2016.ui.events

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
import bitspilani.dvm.apogee2016.R
import bitspilani.dvm.apogee2016.data.firebase.model.Event
import bitspilani.dvm.apogee2016.data.firebase.model.FilterEvents
import bitspilani.dvm.apogee2016.data.firebase.model.ShowBy
import bitspilani.dvm.apogee2016.di.Light
import bitspilani.dvm.apogee2016.di.Regular
import bitspilani.dvm.apogee2016.ui.base.BaseFragment
import bitspilani.dvm.apogee2016.ui.main.CC
import bitspilani.dvm.apogee2016.ui.main.MainActivity
import kotlinx.android.synthetic.main.main_screen_main_content.*
import javax.inject.Inject

/**
 * Created by Vaibhav on 04-02-2018.
 */

class EventsFragment : BaseFragment(), EventsMvpView, ViewPager.OnPageChangeListener, View.OnClickListener {

    @Inject
    lateinit var eventsMvpPresenter: EventsPresenter<EventsMvpView>
    private lateinit var viewPager: ViewPager
    private lateinit var next: ImageView
    private lateinit var prev: ImageView
    private lateinit var heading: TextView
    lateinit var eventClickListener: EventClickListener
    val evaluator = ArgbEvaluator()


    @Inject
    @field:Regular
    lateinit var regularFont: Typeface

    @Inject
    @field:Light
    lateinit var lightFont: Typeface

    override fun onAttach(context: Context) {
        super.onAttach(context)
        eventClickListener = (getBaseActivity() as MainActivity)
        getFragmentComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.events_fragment, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        viewPager.addOnPageChangeListener(this)
        viewPager.addOnPageChangeListener((activity as MainActivity).bib)
        next = view.findViewById(R.id.next)
        prev = view.findViewById(R.id.prev)
        next.setOnClickListener(this)
        prev.setOnClickListener(this)
        heading = view.findViewById(R.id.subLabel)
        heading.typeface = regularFont
        eventsMvpPresenter.onAttach(this)
        (getBaseActivity() as MainActivity).initialize()
        return view
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.next -> {
                val count = viewPager.childCount
                if (viewPager.currentItem < count - 1)
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
        eventsMvpPresenter.onDetach()
    }

    fun setViewPagerAdapter(filterEvents: FilterEvents, data : List<Pair<String, List<Event>>>) {
        val showBy = if(filterEvents.showBy == ShowBy.DATE) 0 else 1
        viewPager.adapter = EventsViewPagerAdapter(data, showBy, eventsMvpPresenter.getDataManager(), lightFont, regularFont, eventClickListener)
        if (data.isNotEmpty())
                heading.text = data[0].first
        else heading.text = "NO RESULTS"
        heading.setTextColor(CC.getScreenColorFor(0).colorB)
        next.setColorFilter(CC.getScreenColorFor(0).colorB)
        prev.setColorFilter(CC.getScreenColorFor(0).colorB)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                activity.window.statusBarColor = CC.getScreenColorFor(0).colorC
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
        val headText = (viewPager.adapter as EventsViewPagerAdapter?)?.queriedEvents?.get(position)?.first ?: ""
        heading.text = headText
        heading.setTextColor(CC.getScreenColorFor(position).colorB)
        next.setColorFilter(CC.getScreenColorFor(position).colorB)
        prev.setColorFilter(CC.getScreenColorFor(position).colorB)
    }
}