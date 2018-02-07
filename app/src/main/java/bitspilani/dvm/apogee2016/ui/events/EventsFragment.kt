package bitspilani.dvm.apogee2016.ui.events

import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bitspilani.dvm.apogee2016.R
import bitspilani.dvm.apogee2016.ui.base.BaseFragment
import javax.inject.Inject

/**
 * Created by Vaibhav on 04-02-2018.
 */

class EventsFragment : BaseFragment(), EventsMvpView {

    @Inject
    lateinit var eventsMvpPresenter: EventsPresenter<EventsMvpView>
    private lateinit var viewPager: ViewPager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getFragmentComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.events_fragment, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        eventsMvpPresenter.onAttach(this)
        viewPager.adapter = EventsViewPagerAdapter(listOf(Pair("dfd", eventsMvpPresenter.getQueries())), 0, eventsMvpPresenter.dataManager())
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        eventsMvpPresenter.onDetach()
    }
}