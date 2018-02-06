package bitspilani.dvm.apogee2016.ui.main

import android.os.Bundle
import android.widget.Toast
import bitspilani.dvm.apogee2016.R
import bitspilani.dvm.apogee2016.ui.base.BaseActivity
import bitspilani.dvm.apogee2016.ui.bottombar.BottomInteractiveBarOnClickListener
import kotlinx.android.synthetic.main.main_screen.*
import javax.inject.Inject

/**
 * Created by Vaibhav on 29-01-2018.
 */

class MainActivity : BaseActivity(), MainMvpView {

    @Inject
    lateinit var mainPresenter: MainPresenter<MainMvpView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)

        getActivityComponent().inject(this)
        mainPresenter.onAttach(this)

        bib.setBottomInteractiveBarOnClickListener(object : BottomInteractiveBarOnClickListener {
            override fun onCenterButtonClick() {
                Toast.makeText(this@MainActivity, "Center Clicked", Toast.LENGTH_SHORT).show()
            }

            override fun onClickItem(position: Int) {
                Toast.makeText(this@MainActivity, "Clicked $position", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.onDetach()
    }

}