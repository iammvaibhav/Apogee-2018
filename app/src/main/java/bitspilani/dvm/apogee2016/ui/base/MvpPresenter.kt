package bitspilani.dvm.apogee2016.ui.base

/**
 * Created by Vaibhav on 24-01-2018.
 */

interface MvpPresenter<V: MvpView> {
    fun onAttach(mvpView: V)
    fun onDetach()
}