package org.dvm.bits_apogee.ui.informatives

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.dvm.bits_apogee.R
import org.dvm.bits_apogee.ui.base.BaseFragment

class AboutFragment : BaseFragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        return view
    }

}