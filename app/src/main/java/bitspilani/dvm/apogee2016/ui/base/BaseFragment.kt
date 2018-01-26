package bitspilani.dvm.apogee2016.ui.base

import android.content.Context
import android.support.v4.app.Fragment

/**
 * Created by Vaibhav on 24-01-2018.
 */

abstract class BaseFragment : Fragment(), MvpView {

    lateinit var mActivity: BaseActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            this.mActivity = context
        }
    }

    override fun onError(message: String) {
        if (mActivity != null) {
            mActivity.onError(message)
        }
    }

    fun getBaseActivity(): BaseActivity {
        return mActivity
    }
}