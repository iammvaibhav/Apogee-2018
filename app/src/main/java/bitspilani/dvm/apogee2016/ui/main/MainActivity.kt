package bitspilani.dvm.apogee2016.ui.main

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Fragment
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
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
import bitspilani.dvm.apogee2016.ui.informatives.*
import bitspilani.dvm.apogee2016.ui.login.LoginActivity
import bitspilani.dvm.apogee2016.ui.profile.ProfileFragment
import com.awesomecorp.sammy.apogeewallet.WalletActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import example.aditya.com.sms.MainActivity
import kotlinx.android.synthetic.main.event_bottom_sheet.*
import kotlinx.android.synthetic.main.left_drawer.*
import kotlinx.android.synthetic.main.main_screen.*
import kotlinx.android.synthetic.main.main_screen_main_content.*
import kotlinx.android.synthetic.main.right_drawer.*
import javax.inject.Inject

/**
 * Created by Vaibhav on 29-01-2018.
 */

class MainActivity : BaseActivity(), MainMvpView, View.OnClickListener, EventClickListener, OnMapReadyCallback {

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
    val profileFragment by lazy { ProfileFragment() }
    val mapFragment by lazy { MapFragment() }

    var filterEvents = FilterEvents()
    var filterEventsCurrSession = FilterEvents()

    val venueList = mutableListOf<String>()
    val categoryList = mutableListOf<String>()

    var mGoogleMap: GoogleMap? = null
    var mLocationRequest: LocationRequest? = null
    var mLastLocation: Location? = null
    var mCurrLocationMarker: Marker? = null
    var mFusedLocationClient: FusedLocationProviderClient? = null

    var currColor = CC.getScreenColorFor(0).colorC

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

        setHeading("events")
        fragmentManager.beginTransaction().replace(R.id.container, eventsFragment).commit()
        heading.typeface = semiBold

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        Thread {
            val options = BitmapFactory.Options()
            options.inSampleSize = 2
            backgroundArt = BitmapFactory.decodeResource(resources, R.drawable.background_art)

            try {
                eventBottomSheet.post {
                    eventBottomSheet.background = getBottomSheetBackground(CC.getScreenColorFor(0).colorC)
                }

                leftDrawer.post {
                    leftDrawer.setImageBitmap(getLeftNavDrawerBackground(CC.getScreenColorFor(0).colorC))
                }

                rightDrawer.post {
                    rightDrawer.setImageBitmap(getRightNavDrawerBackground(CC.getScreenColorFor(0).colorC))
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }

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
        about.setOnClickListener(this)
        contactUs.setOnClickListener(this)
        sponsors.setOnClickListener(this)
        profShows.setOnClickListener(this)
        schedule.setOnClickListener(this)
        notifications.setOnClickListener(this)
        emergency.setOnClickListener(this)
        developers.setOnClickListener(this)
        blog.setOnClickListener(this)
        sms.setOnClickListener(this)

        drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED, Gravity.END)


        bib.setBottomInteractiveBarOnClickListener(object : BottomInteractiveBarOnClickListener {
            override fun onCenterButtonClick() {
                if (mainPresenter.getDataManager().getUserLoggedIn())
                    startActivity(Intent(this@MainActivity, WalletActivity::class.java))
                else
                    startActivityForResult(Intent(this@MainActivity, LoginActivity::class.java), 99)
            }

            override fun onClickItem(position: Int) {
                when(position) {
                    0 -> {
                        setHeading("profile")
                        addFragment(profileFragment)
                    }
                    1 -> {
                        setHeading("events")
                        addFragment(eventsFragment)
                        val filterE = FilterEvents()
                        mainPresenter.getDataManager().getEvents(filterE) {
                            filterEvents = filterE
                            eventsFragment.setViewPagerAdapter(filterE, it)
                        }
                    }
                    2 -> {
                        setHeading("map")
                        val mapFragment = mapFragment
                        mapFragment.getMapAsync(this@MainActivity)
                        addFragment(mapFragment)
                    }
                    3 -> {
                        setHeading("ongoing")
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
            R.id.hamburger -> {
                drawerLayout.openDrawer(Gravity.START)
            }
            R.id.closeLeft -> drawerLayout.closeDrawer(Gravity.START)
            R.id.closeRight -> drawerLayout.closeDrawer(Gravity.END)
            R.id.options -> {
                eventBottomSheet.background = getBottomSheetBackground(currColor)
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
            R.id.about -> {
                setHeading("About")
                addFragment(AboutFragment())
                drawerLayout.closeDrawer(Gravity.START)
            }
            R.id.contactUs -> {
                setHeading("Contact Us")
                addFragment(ContactFragment())
                drawerLayout.closeDrawer(Gravity.START)
            }
            R.id.sponsors -> {
                setHeading("Sponsors")
                addFragment(SponsorsFragment())
                drawerLayout.closeDrawer(Gravity.START)
            }
            R.id.profShows -> {
                setHeading("Prof Shows")
                addFragment(eventsFragment)
                val filterE = FilterEvents()
                filterE.showBy = ShowBy.CATEGORY
                mainPresenter.getDataManager().getCategoryList { val x = it.toMutableList()
                    x.forEach { if (it.contains("prof show", true)) x.remove(it) }
                    filterE.excludeCategory.addAll(x)
                    mainPresenter.getDataManager().getEvents(filterE) {
                        filterEvents = filterE
                        eventsFragment.setViewPagerAdapter(filterE, it)
                    }
                }
                drawerLayout.closeDrawer(Gravity.START)
            }
            R.id.schedule -> {
                setHeading("events")
                addFragment(eventsFragment)
                val filterE = FilterEvents()
                mainPresenter.getDataManager().getEvents(filterE) {
                    filterEvents = filterE
                    eventsFragment.setViewPagerAdapter(filterE, it)
                }
                drawerLayout.closeDrawer(Gravity.START)
            }
            R.id.notifications -> {
                setHeading("notifications")
                addFragment(NotificationsFragment())
                drawerLayout.closeDrawer(Gravity.START)
            }
            R.id.blog -> {
                setHeading("epc blog")
                addFragment(BlogFragment())
                drawerLayout.closeDrawer(Gravity.START)
            }
            R.id.emergency -> {
                setHeading("emergency")
                addFragment(EmergencyFragment())
                drawerLayout.closeDrawer(Gravity.START)
            }
            R.id.developers -> {
                setHeading("developers")
                addFragment(DevelopersFragment())
                drawerLayout.closeDrawer(Gravity.START)
            }
            R.id.sms -> {
                startActivity(Intent(this@MainActivity, MainActivity::class.java))
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

    override fun onPause() {
        super.onPause()
        try {
            mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mGoogleMap = googleMap
        mGoogleMap?.setMapType(GoogleMap.MAP_TYPE_NORMAL)

        mLocationRequest = LocationRequest()
        mLocationRequest?.setInterval(120000) // two minute interval
        mLocationRequest?.setFastestInterval(120000)
        mLocationRequest?.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                mGoogleMap?.setMyLocationEnabled(true)
            } else {
                //Request Location Permission
                checkLocationPermission()
            }
        } else {
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
            mGoogleMap?.setMyLocationEnabled(true)
        }

//        LatLng me=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
        val gymg = LatLng(28.359211, 75.590495)
        val medc =  LatLng(28.357417, 75.591219)
        val srground = LatLng(28.365923,75.587759)
        val anc = LatLng(28.360346, 75.589632)
        val sac = LatLng(28.360710, 75.585639)
        val fd3 = LatLng(28.363988, 75.586274)
        val clocktower = LatLng(28.363906, 75.586980)
        val fd2 = LatLng(28.364059, 75.587873)
        val uco = LatLng(28.363257, 75.590715)
        val icici = LatLng(28.357139, 75.590436)
        val axis = LatLng(28.361605, 75.585046)
        val fk = LatLng(28.361076, 75.585457)
        val ltc = LatLng(28.365056, 75.590092)
        val nab = LatLng(28.362228, 75.587346)
        val swimmingPool = LatLng(28.3607699,75.5913962)
        val mlawns = LatLng(28.363479, 75.588169)


        val cameraPosition = CameraPosition.Builder().
                target(clocktower).
                tilt(60f).
                zoom(17f).
                bearing(0f).
                build()

        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        googleMap?.addMarker(MarkerOptions().position(anc).title("ANC").snippet("All Night Canteen"))
        googleMap?.addMarker(MarkerOptions().position(sac).title("SAC").snippet("Student Activity Center"))
        googleMap?.addMarker(MarkerOptions().position(fd3).title("FD3").snippet("Faculty Division-III(31xx-32xx)"))
        googleMap?.addMarker(MarkerOptions().position(clocktower).title("Clock Tower").snippet("Auditorium"))
        googleMap?.addMarker(MarkerOptions().position(fd2).title("FD2").snippet("Faculty Division-II(21xx-22xx)"))
        googleMap?.addMarker(MarkerOptions().position(uco).title("UCO Bank ATM"))
        googleMap?.addMarker(MarkerOptions().position(icici).title("ICICI ATM"))
        googleMap?.addMarker(MarkerOptions().position(axis).title("AXIS Bank ATM"))
        googleMap?.addMarker(MarkerOptions().position(fk).title("FoodKing").snippet("Restaurant"))
        googleMap?.addMarker(MarkerOptions().position(ltc).title("LTC").snippet("Lecture Theater Complex(510x)"))
        googleMap?.addMarker(MarkerOptions().position(nab).title("NAB").snippet("New Academic Block(60xx-61xx)"))
        googleMap?.addMarker(MarkerOptions().position(gymg).title("GYMG").snippet("Gym Grounds"))
        googleMap?.addMarker(MarkerOptions().position(medc).title("MedC").snippet("Medical Center"))
        googleMap?.addMarker(MarkerOptions().position(srground).title("SR Grounds").snippet("SR Bhawan Grounds"))
        googleMap?.addMarker(MarkerOptions().position(swimmingPool).title("Swimming Pool").snippet("Bits Swimming Pool"))
        googleMap?.addMarker(MarkerOptions().position(mlawns).title("M Lawns").snippet("M Lawns"))
//        mMap.addMarker(new MarkerOptions().position(me).title("You are here!").snippet("Consider yourself located"));
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap?.isBuildingsEnabled = true
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

        /*val path = PathParser.createPathFromPathData(pathData)
        val shape = ShapeDrawable(PathShape(path, eventBottomSheet.width.toFloat(), eventBottomSheet.height.toFloat()))
        shape.setBounds(0, 0, eventBottomSheet.width, eventBottomSheet.height)
        shape.paint.color = Color.BLACK
        shape.paint.style = Paint.Style.FILL

        val bitmapTemp = Bitmap.createBitmap(shape.bounds.width(), shape.bounds.height(), Bitmap.Config.ARGB_8888)
        val canvasTemp = Canvas(bitmapTemp)
        shape.draw(canvasTemp)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        canvas.drawBitmap(bitmapTemp, 0f, 0f, paint)*/

        return BitmapDrawable(resources, bitmap)
    }

    fun getLeftNavDrawerBackground(color: Int): Bitmap {

        val pathData = "M-501,2146v-2146L29.9,0L29.9,-247.1h791.8v288.1a163.1,163.1 0,0 0,83.8 142.4,172.9 172.9,0 0,1 89.1,151.3 172.9,172.9 0,0 1,-89.1 151.3,162.9 162.9,0 0,0 -83.8,142.4v796.9h0.5v107.3h-0.9L821.3,1816.6L821,1816.6L821,2128L293,2128v18Z"
        val bitmap = Bitmap.createBitmap(leftDrawer.width, leftDrawer.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)

        val paint = Paint()
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        paint.shader = BitmapShader(backgroundArt, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        canvas.drawARGB(255, Color.red(color), Color.green(color), Color.blue(color))
        canvas.drawRect(0f, 0f, leftDrawer.width.toFloat(), leftDrawer.height.toFloat(), paint)


        /*val path = PathParser.createPathFromPathData(pathData)
        val shape = ShapeDrawable(PathShape(path, 500.toPx().toFloat(), leftDrawer.height.toFloat()))
        shape.setBounds(0, 0, 500.toPx(), leftDrawer.height)
        shape.paint.color = Color.BLACK
        shape.paint.style = Paint.Style.FILL

        val bitmapTemp = Bitmap.createBitmap(shape.bounds.width(), shape.bounds.height(), Bitmap.Config.ARGB_8888)
        val canvasTemp = Canvas(bitmapTemp)
        shape.draw(canvasTemp)
        val src = Rect(0, 0, bitmapTemp.width, bitmapTemp.height)
        val dest = Rect(0, 0, leftDrawer.width, leftDrawer.height)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        canvas.drawBitmap(bitmapTemp, src, dest, paint)*/

        /*return BitmapDrawable(resources, bitmap)*/
        return bitmap
    }

    fun getRightNavDrawerBackground(color: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(rightDrawer.width, rightDrawer.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)

        val paint = Paint()
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        paint.shader = BitmapShader(backgroundArt, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        canvas.drawARGB(255, Color.red(color), Color.green(color), Color.blue(color))
        canvas.drawRect(0f, 0f, rightDrawer.width.toFloat(), rightDrawer.height.toFloat(), paint)

        /*val mask = decodeSampledBitmapFromResource(resources, R.drawable.right_drawer_mask)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        canvas.drawBitmap(mask, 0f, 0f, paint)*/

        return bitmap
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
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(this@MainActivity,
                                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                                    MY_PERMISSIONS_REQUEST_LOCATION)
                        })
                        .create()
                        .show()


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION)
            }
        }
    }

    var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            for (location in locationResult!!.locations) {
                Log.i("MapsActivity", "Location: " + location.latitude + " " + location.longitude)
                mLastLocation = location

                mCurrLocationMarker?.remove()

                //Place current location marker
                val latLng = LatLng(location.latitude, location.longitude)
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.title("Current Position")
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                mCurrLocationMarker = mGoogleMap?.addMarker(markerOptions)

                //move map camera
                val cameraPosition = CameraPosition.Builder().
                        target(latLng).
                        tilt(60f).
                        zoom(17f).
                        bearing(0f).
                        build()

                mGoogleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                        mGoogleMap?.setMyLocationEnabled(true)
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 99) {
            if (resultCode == Activity.RESULT_OK)
                startActivity(Intent(this@MainActivity, WalletActivity::class.java))
            else Toast.makeText(this, "Login is required for Wallet", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START))
            drawerLayout.closeDrawer(Gravity.START)
        else if(drawerLayout.isDrawerOpen(Gravity.END))
            drawerLayout.closeDrawer(Gravity.END)
        else super.onBackPressed()
    }

}