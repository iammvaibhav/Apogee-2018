package org.dvm.bits_apogee.ui.base

import org.dvm.bits_apogee.data.DataManager

/**
 * Created by Vaibhav on 24-01-2018.
 */

open class BasePresenter<V: MvpView> constructor(private val mDataManager: DataManager) : MvpPresenter<V> {

    private var mMvpView: V? = null

    override fun onAttach(mvpView: V) {
        mMvpView = mvpView
    }

    override fun onDetach() {
        mMvpView = null
    }

    fun isViewAttached() = mMvpView != null

    fun getMvpView(): V? {
        return mMvpView
    }

    fun getDataManager() = mDataManager
}