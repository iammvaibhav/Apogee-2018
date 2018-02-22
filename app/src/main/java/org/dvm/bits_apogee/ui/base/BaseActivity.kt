package org.dvm.bits_apogee.ui.base

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import org.dvm.bits_apogee.App
import org.dvm.bits_apogee.di.component.ActivityComponent
import org.dvm.bits_apogee.di.component.DaggerActivityComponent
import org.dvm.bits_apogee.di.module.ActivityModule

/**
 * Created by Vaibhav on 24-01-2018.
 */

abstract class BaseActivity : AppCompatActivity(), MvpView {

    private lateinit var mActivityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .applicationComponent((applicationContext as App).getApplicationComponent())
                .build()
    }

    fun getActivityComponent() = mActivityComponent

    private fun showSnackBar(message: String) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT)
        val sbView = snackbar.view
        val textView = sbView
                .findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        snackbar.show()
    }

    override fun onError(message: String?) {
        if (message != null) {
            showSnackBar(message)
        } else {
            showSnackBar("Some Error Occurred")
        }
    }
}