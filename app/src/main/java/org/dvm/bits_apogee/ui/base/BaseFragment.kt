package org.dvm.bits_apogee.ui.base

import android.app.Fragment
import android.content.Context





/**
 * Created by Vaibhav on 24-01-2018.
 */

abstract class BaseFragment : Fragment(), MvpView {

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onError(message: String?) {
        if (activity != null && activity is BaseActivity) {
            (activity as BaseActivity).onError(message)
        }
    }
}