package bitspilani.dvm.apogee2016.ui.informatives

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import bitspilani.dvm.apogee2016.R
import bitspilani.dvm.apogee2016.di.SemiBold
import bitspilani.dvm.apogee2016.ui.base.BaseFragment
import bitspilani.dvm.apogee2016.ui.main.MainActivity
import javax.inject.Inject

class AboutFragment : BaseFragment(){

    @Inject
    @field:SemiBold
    lateinit var typeface: Typeface

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getFragmentComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        (getBaseActivity() as MainActivity).setHeading("About")

        val view = inflater.inflate(R.layout.fragment_about, container, false)
        view.findViewById<TextView>(R.id.aboutText).typeface = typeface
        return view
    }
}