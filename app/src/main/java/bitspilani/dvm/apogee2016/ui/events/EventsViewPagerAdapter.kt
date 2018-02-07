package bitspilani.dvm.apogee2016.ui.events

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import bitspilani.dvm.apogee2016.R
import bitspilani.dvm.apogee2016.data.DataManager
import bitspilani.dvm.apogee2016.data.realmdb.model.Event
import io.realm.OrderedRealmCollection

/**
 * Created by Vaibhav on 04-02-2018.
 */

class EventsViewPagerAdapter(private val queriedEvents: List<Pair<String, OrderedRealmCollection<Event>>>,
                             private val showBy: Int,
                             private val dataManager: DataManager)  : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val recyclerView = RecyclerView(container.context)
        val layoutParams = ViewGroup.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT)
        recyclerView.layoutParams = layoutParams
        recyclerView.layoutManager = LinearLayoutManager(container.context)
        val lrPadding = container.context.resources.getDimension(R.dimen.recyclerViewLRPadding).toInt()
        val tPadding = container.context.resources.getDimension(R.dimen.recyclerViewTopPadding).toInt()
        recyclerView.setPadding(lrPadding, tPadding, lrPadding, 0)
        recyclerView.adapter = EventsRecylerViewAdapter(queriedEvents[position].second, showBy, dataManager)
        container.addView(recyclerView)
        return recyclerView
    }

    override fun isViewFromObject(view: View, `object`: Any) = view == (`object` as View)

    override fun getCount() = queriedEvents.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}