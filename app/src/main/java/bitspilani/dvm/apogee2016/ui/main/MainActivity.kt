package bitspilani.dvm.apogee2016.ui.main

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import bitspilani.dvm.apogee2016.R
import bitspilani.dvm.apogee2016.data.firebase.model.Event
import bitspilani.dvm.apogee2016.data.firebase.model.FilterEvents
import bitspilani.dvm.apogee2016.data.firebase.model.ShowBy
import bitspilani.dvm.apogee2016.di.SemiBold
import bitspilani.dvm.apogee2016.ui.base.BaseActivity
import bitspilani.dvm.apogee2016.ui.bottombar.BottomInteractiveBarOnClickListener
import bitspilani.dvm.apogee2016.ui.events.EventClickListener
import bitspilani.dvm.apogee2016.ui.events.EventsFragment
import bitspilani.dvm.apogee2016.utils.PathParser
import kotlinx.android.synthetic.main.event_bottom_sheet.*
import kotlinx.android.synthetic.main.left_drawer.*
import kotlinx.android.synthetic.main.main_screen.*
import kotlinx.android.synthetic.main.main_screen_main_content.*
import kotlinx.android.synthetic.main.right_drawer.*
import org.jetbrains.anko.backgroundDrawable
import javax.inject.Inject


/**
 * Created by Vaibhav on 29-01-2018.
 */

class MainActivity : BaseActivity(), MainMvpView, View.OnClickListener, EventClickListener {

    class ScreenColor(colorA: String, colorB: String, colorC: String) {
        val colorA = Color.parseColor(colorA)
        val colorB = Color.parseColor(colorB)
        val colorC = Color.parseColor(colorC)
    }

    @Inject
    lateinit var mainPresenter: MainPresenter<MainMvpView>

    lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    lateinit var backgroundArt: Bitmap

    //Bottom Sheet Filter View
    lateinit var bottomSheetFilterView: View

    //Filter Events Bottom Sheet
    lateinit var close: ImageView
    lateinit var showByDate: ToggleButton
    lateinit var showByCategory: ToggleButton
    lateinit var filterByVenue: Button
    lateinit var filterByCategory: Button
    lateinit var filterByFavourites: ToggleButton
    lateinit var filterByOngoing: ToggleButton
    lateinit var apply: Button
    lateinit var clear: Button

    //Bottom Sheet Exclude View
    lateinit var bottomSheetExcludeView: View
    lateinit var close2: ImageView

    //Exclude View Bottom Sheet
    lateinit var back: LinearLayout
    lateinit var recyclerView: RecyclerView

    val eventsFragment by lazy { EventsFragment() }

    var filterEvents = FilterEvents()
    var filterEventsCurrSession = FilterEvents()

    val venueList = mutableListOf<String>()
    val categoryList = mutableListOf<String>()

    @Inject
    @field:SemiBold
    lateinit var semiBold: Typeface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)

        getActivityComponent().inject(this)
        mainPresenter.onAttach(this)

        bottomSheetBehavior = BottomSheetBehavior.from(eventBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        supportFragmentManager.beginTransaction().replace(R.id.container, eventsFragment).commit()
        heading.typeface = semiBold

        Thread {
            val options = BitmapFactory.Options()
            options.inSampleSize = 2
            backgroundArt = BitmapFactory.decodeResource(resources, R.drawable.background_art)

            eventBottomSheet.post {
                eventBottomSheet.backgroundDrawable = getBottomSheetBackground(Color.BLACK)
            }

            /*leftDrawer.post {
                leftDrawer.backgroundDrawable = getLeftNavDrawerBackground(Color.parseColor("#0CA9EF"))
            }

            rightDrawer.post {
                rightDrawer.backgroundDrawable = getRightNavDrawerBackground(Color.parseColor("#0CEFB9"))
            }*/

            bottomSheetFilterView = LayoutInflater.from(this).inflate(R.layout.filter_events_bottom_sheet, eventBottomSheet, false)

            with(bottomSheetFilterView) {
                close = findViewById(R.id.closeButton)
                showByDate = findViewById(R.id.showByDate)
                showByCategory = findViewById(R.id.showByCategory)
                filterByCategory = findViewById(R.id.categoryFilter)
                filterByVenue = findViewById(R.id.venueFilter)
                filterByOngoing = findViewById(R.id.ongoingFilter)
                filterByFavourites = findViewById(R.id.favouritesFilter)
                apply = findViewById(R.id.apply)
                clear = findViewById(R.id.clear)
            }

            close.setOnClickListener(this@MainActivity)
            showByDate.setOnClickListener(this@MainActivity)
            showByCategory.setOnClickListener(this@MainActivity)
            filterByCategory.setOnClickListener(this@MainActivity)
            filterByVenue.setOnClickListener(this@MainActivity)
            filterByOngoing.setOnClickListener(this@MainActivity)
            filterByFavourites.setOnClickListener(this@MainActivity)
            apply.setOnClickListener(this@MainActivity)
            clear.setOnClickListener(this@MainActivity)

            bottomSheetExcludeView = LayoutInflater.from(this).inflate(R.layout.exclude_selection_bottom_sheet, eventBottomSheet, false)

            with(bottomSheetExcludeView) {
                close2 = findViewById(R.id.closeButton2)
                back = findViewById(R.id.back)
                recyclerView = findViewById(R.id.recyclerView)
            }

            close2.setOnClickListener(this)
            back.setOnClickListener(this@MainActivity)

            mainPresenter.getVenueList { venueList.addAll(it) }
            mainPresenter.getCategoryList { categoryList.addAll(it) }

        }.start()

        hamburger.setOnClickListener(this)
        options.setOnClickListener(this)
        closeLeft.setOnClickListener(this)
        closeRight.setOnClickListener(this)

        bib.setBottomInteractiveBarOnClickListener(object : BottomInteractiveBarOnClickListener {
            override fun onCenterButtonClick() {
                Toast.makeText(this@MainActivity, "Center Clicked", Toast.LENGTH_SHORT).show()
            }

            override fun onClickItem(position: Int) {
                when(position) {
                    0 -> {}
                    1 -> {
                        addFragment(eventsFragment)
                        val filterE = FilterEvents()
                        mainPresenter.getDataManager().getEvents(filterE) {
                            filterEvents = filterE
                            eventsFragment.setViewPagerAdapter(filterE, it)
                        }
                    }
                    2 -> {}
                    3 -> {
                        addFragment(eventsFragment)
                        val filterE = FilterEvents()
                        filterE.filterByOngoing = true
                        mainPresenter.getDataManager().getEvents(filterE) {
                            filterEvents = filterE
                            eventsFragment.setViewPagerAdapter(filterE, it)
                        }
                    }
                }
            }
        })
    }

    fun initialize() {
        mainPresenter.getDataManager().getEvents(filterEvents) {
            eventsFragment.setViewPagerAdapter(filterEvents, it)
        }
    }

    fun setHeading(to: String) {
        heading.text = to
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.hamburger -> drawerLayout.openDrawer(Gravity.START)
            R.id.closeLeft -> drawerLayout.closeDrawer(Gravity.START)
            R.id.closeRight -> drawerLayout.closeDrawer(Gravity.END)
            R.id.options -> {
                filterEventsCurrSession = filterEvents.copy()
                eventBottomSheet.removeAllViews()
                eventBottomSheet.addView(bottomSheetFilterView)
                showByDate.isChecked = filterEventsCurrSession.showBy == ShowBy.DATE
                showByCategory.isChecked = filterEventsCurrSession.showBy == ShowBy.CATEGORY
                filterByFavourites.isChecked = filterEventsCurrSession.filterByFavourites
                filterByOngoing.isChecked = filterEventsCurrSession.filterByOngoing

                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            R.id.closeButton -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            R.id.closeButton2 -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            R.id.showByDate -> {
                if (showByDate.isChecked) {
                    showByCategory.isChecked = false
                    filterEventsCurrSession.showBy = ShowBy.DATE
                }
            }
            R.id.showByCategory -> {
                if (showByCategory.isChecked) {
                    showByDate.isChecked = false
                    filterEventsCurrSession.showBy = ShowBy.CATEGORY
                }
            }
            R.id.venueFilter -> {
                eventBottomSheet.removeAllViews()
                eventBottomSheet.addView(bottomSheetExcludeView)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = ExcludeRecyclerViewAdapter(venueList, filterEventsCurrSession, 0)
            }
            R.id.categoryFilter -> {
                eventBottomSheet.removeAllViews()
                eventBottomSheet.addView(bottomSheetExcludeView)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = ExcludeRecyclerViewAdapter(categoryList, filterEventsCurrSession, 1)
            }
            R.id.ongoingFilter -> filterEventsCurrSession.filterByOngoing = !filterEventsCurrSession.filterByOngoing
            R.id.favouritesFilter -> filterEventsCurrSession.filterByFavourites = !filterEventsCurrSession.filterByFavourites
            R.id.clear -> {
                filterEventsCurrSession.clearAll()
                showByDate.isChecked = true
                showByCategory.isChecked = false
                filterByOngoing.isChecked = false
                filterByFavourites.isChecked = false
            }
            R.id.apply -> {
                Log.e("filters", filterEventsCurrSession.toString())
                mainPresenter.fetchQueries(filterEventsCurrSession) {
                    filterEvents = filterEventsCurrSession
                    //TODO("reflect fetched data")
                    eventsFragment.setViewPagerAdapter(filterEvents, it)
                    Log.e("filtered", it.toString())
                }
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
            R.id.back -> {
                eventBottomSheet.removeAllViews()
                eventBottomSheet.addView(bottomSheetFilterView)
            }
        }
    }

    override fun onEventClick(event: Event, isFavourite: Boolean, tim: String) {
        eventName1.text = event.name
        venue1.text = event.venue
        time1.text = tim
        description.text = event.description
        remindMe.isChecked = isFavourite
        drawerLayout.openDrawer(Gravity.END)
        rules.setOnClickListener {
            if (rules.text == "rules") {
                rules.text = "description"
                description.text = event.rules
            }
            else {
                rules.text = "rules"
                description.text = event.description
            }
        }
        remindMe.setOnCheckedChangeListener { buttonView, isChecked ->
            //TODO("Set notification")
            if (isChecked)
                mainPresenter.getDataManager().addAsFavourite(event.id)
            else
                mainPresenter.getDataManager().removeAsFavourite(event.id)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.onDetach()
    }

    private fun getBottomSheetBackground(color: Int): Drawable {
        val pathData = "M0,2018.4v-1915h358.9a99.5,99.5 0,0 0,66.9 -49.1A105.4,105.4 0,0 1,518.1 0a105.4,105.4 0,0 1,92.3 54.3A99.3,99.3 0,0 0,677.2 103.4L1080,103.4v1915Z"
        //val pathData = "M0,1318.4v-1215h358.9a99.5,99.5 0,0 0,66.9 -49.1A105.4,105.4 0,0 1,518.1 0a105.4,105.4 0,0 1,92.3 54.3A99.3,99.3 0,0 0,677.2 103.4L1080,103.4v1215Z"

        val bitmap = Bitmap.createBitmap(eventBottomSheet.width, eventBottomSheet.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)

        val paint = Paint()
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        paint.shader = BitmapShader(backgroundArt, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        canvas.drawARGB(255, Color.red(color), Color.green(color), Color.blue(color))
        canvas.drawRect(0f, 0f, eventBottomSheet.width.toFloat(), eventBottomSheet.height.toFloat(), paint)
        //canvas.drawBitmap(backgroundArt, 0f, 0f, paint)

        val path = PathParser.createPathFromPathData(pathData)
        val shape = ShapeDrawable(PathShape(path, eventBottomSheet.width.toFloat(), eventBottomSheet.height.toFloat()))
        shape.setBounds(0, 0, eventBottomSheet.width, eventBottomSheet.height)
        shape.paint.color = Color.BLACK
        shape.paint.style = Paint.Style.FILL

        val bitmapTemp = Bitmap.createBitmap(shape.bounds.width(), shape.bounds.height(), Bitmap.Config.ARGB_8888)
        val canvasTemp = Canvas(bitmapTemp)
        shape.draw(canvasTemp)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        canvas.drawBitmap(bitmapTemp, 0f, 0f, paint)

        return BitmapDrawable(resources, bitmap)
    }

    private fun getLeftNavDrawerBackground(color: Int): Drawable {

        val bitmap = Bitmap.createBitmap(leftDrawer.width, leftDrawer.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)

        val paint = Paint()
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        paint.shader = BitmapShader(backgroundArt, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        canvas.drawARGB(255, Color.red(color), Color.green(color), Color.blue(color))
        canvas.drawRect(0f, 0f, leftDrawer.width.toFloat(), leftDrawer.height.toFloat(), paint)

        val mask = decodeSampledBitmapFromResource(resources, R.drawable.left_drawer_mask)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        canvas.drawBitmap(mask, 0f, 0f, paint)

        return BitmapDrawable(resources, mask)
    }

    private fun getRightNavDrawerBackground(color: Int): Drawable {
        val bitmap = Bitmap.createBitmap(rightDrawer.width, rightDrawer.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)

        val paint = Paint()
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        paint.shader = BitmapShader(backgroundArt, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        canvas.drawARGB(255, Color.red(color), Color.green(color), Color.blue(color))
        canvas.drawRect(0f, 0f, rightDrawer.width.toFloat(), rightDrawer.height.toFloat(), paint)

        val mask = decodeSampledBitmapFromResource(resources, R.drawable.right_drawer_mask)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        canvas.drawBitmap(mask, 0f, 0f, paint)

        return BitmapDrawable(resources, mask)
    }

    fun decodeSampledBitmapFromResource(res: Resources, resId: Int): Bitmap {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

}