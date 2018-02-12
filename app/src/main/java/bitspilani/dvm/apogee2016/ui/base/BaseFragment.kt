package bitspilani.dvm.apogee2016.ui.base

import android.app.Fragment
import android.content.Context
import bitspilani.dvm.apogee2016.di.component.DaggerFragmentComponent
import bitspilani.dvm.apogee2016.di.component.FragmentComponent





/**
 * Created by Vaibhav on 24-01-2018.
 */

abstract class BaseFragment : Fragment(), MvpView {

    private lateinit var mActivity: BaseActivity
    private lateinit var mFragmentComponent: FragmentComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            this.mActivity = context
        }
        mFragmentComponent = DaggerFragmentComponent.builder()
                .activityComponent(getBaseActivity().getActivityComponent())
                .build()
    }

    override fun onError(message: String?) {
        if (mActivity != null) {
            mActivity.onError(message)
        }
    }

    fun getBaseActivity(): BaseActivity {
        return mActivity
    }

    fun getFragmentComponent() = mFragmentComponent


}