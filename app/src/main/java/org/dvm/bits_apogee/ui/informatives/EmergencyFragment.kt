package org.dvm.bits_apogee.ui.informatives


import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.dvm.bits_apogee.databinding.FragmentEmergencyBinding
import org.dvm.bits_apogee.ui.base.BaseFragment

class EmergencyFragment : BaseFragment(){

    lateinit var typeface: Typeface

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {


        val listener = View.OnClickListener{ v ->
            val phoneNo = (v as TextView).text
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNo")
            activity?.startActivity(intent)
        }

        typeface = Typeface.DEFAULT_BOLD
        val binding = FragmentEmergencyBinding.inflate(inflater, container, false)
        binding.typeface = typeface
        binding.listener = listener

        return binding.root
    }
}