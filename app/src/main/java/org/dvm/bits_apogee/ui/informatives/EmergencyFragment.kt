package org.dvm.bits_apogee.ui.informatives


import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.dvm.bits_apogee.databinding.FragmentEmergencyBinding
import org.dvm.bits_apogee.di.SemiBold
import org.dvm.bits_apogee.ui.base.BaseFragment
import org.dvm.bits_apogee.ui.main.MainActivity
import javax.inject.Inject

class EmergencyFragment : BaseFragment(){


    @Inject
    @field:SemiBold
    lateinit var typeface: Typeface

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getFragmentComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        (getBaseActivity() as MainActivity).setHeading("Emergency")

        val listener = View.OnClickListener{ v ->
            val phoneNo = (v as TextView).text
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNo")
            activity?.startActivity(intent)
        }

        val binding = FragmentEmergencyBinding.inflate(inflater, container, false)
        binding.typeface = typeface
        binding.listener = listener

        return binding.root
    }
}