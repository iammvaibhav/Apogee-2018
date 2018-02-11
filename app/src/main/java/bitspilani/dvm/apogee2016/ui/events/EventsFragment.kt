package bitspilani.dvm.apogee2016.ui.events

import android.content.Context
import android.graphics.Typeface
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
import bitspilani.dvm.apogee2016.ui.main.MainActivity
import javax.inject.Inject

/**
 * Created by Vaibhav on 04-02-2018.
 */

class EventsFragment : BaseFragment(), EventsMvpView, ViewPager.OnPageChangeListener {

    @Inject
    lateinit var eventsMvpPresenter: EventsPresenter<EventsMvpView>
    lateinit var initialization: EventInitialization
    private lateinit var viewPager: ViewPager
    private lateinit var next: ImageView
    private lateinit var prev: ImageView
    private lateinit var heading: TextView


    @Inject
    @field:Regular
    lateinit var regularFont: Typeface

    @Inject
    @field:Light
    lateinit var lightFont: Typeface

    lateinit var currViewPagerData: List<Pair<String, List<Event>>>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initialization = getBaseActivity() as MainActivity
        (getBaseActivity() as MainActivity).setHeading("EVENTS")
        getFragmentComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.events_fragment, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        viewPager.addOnPageChangeListener(this)
        next = view.findViewById(R.id.next)
        prev = view.findViewById(R.id.prev)
        heading = view.findViewById(R.id.subLabel)
        heading.typeface = regularFont

        eventsMvpPresenter.onAttach(this)
        initialization.initialize()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        eventsMvpPresenter.onDetach()
    }

    fun setViewPagerAdapter(filterEvents: FilterEvents, data : List<Pair<String, List<Event>>>) {
        val showBy = if(filterEvents.showBy == ShowBy.DATE) 0 else 1
        viewPager.adapter = EventsViewPagerAdapter(data, showBy, eventsMvpPresenter.getDataManager(), lightFont, regularFont)
        heading.text = data[0].first
        currViewPagerData = data
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        heading.text = currViewPagerData[position].first
    }
}