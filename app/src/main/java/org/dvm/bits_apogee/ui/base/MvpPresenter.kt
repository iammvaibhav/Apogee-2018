package org.dvm.bits_apogee.ui.base

/**
 * Created by Vaibhav on 24-01-2018.
 */

interface MvpPresenter<V: MvpView> {
    fun onAttach(mvpView: V)
    fun onDetach()
}