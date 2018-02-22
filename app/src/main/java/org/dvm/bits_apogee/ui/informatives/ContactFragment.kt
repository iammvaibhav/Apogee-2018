package org.dvm.bits_apogee.ui.informatives

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import org.dvm.bits_apogee.R
import org.dvm.bits_apogee.databinding.ContactItemBinding
import org.dvm.bits_apogee.databinding.ContactItemFirstBinding
import org.dvm.bits_apogee.di.SemiBold
import org.dvm.bits_apogee.ui.base.BaseFragment
import org.dvm.bits_apogee.ui.main.MainActivity
import com.squareup.picasso.Picasso
import javax.inject.Inject


interface phoneOnClickListener{
    fun onPhoneClick(phoneNo: String)
}

interface emailOnClickListener{
    fun onEmailClick(email: String)
}
data class ContactData(val photo: Int, val name: String, val department: String, val email: String, val phoneNo: String, val emailListener: View.OnClickListener, val phoneListener: View.OnClickListener)

class ContactViewHolder(val binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bindData(data: ContactData, typeface: Typeface){
        binding.dataItem = data
        binding.typeface = typeface
    }
}

class ContactFirstViewHolder(val binding: ContactItemFirstBinding) : RecyclerView.ViewHolder(binding.root){
    fun bindData(data: ContactData, typeface: Typeface){
        binding.dataItem = data
        binding.typeface = typeface
    }
}

class ContactAdapter(val data: Array<ContactData>, val typeface: Typeface, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContactFirstViewHolder){
            holder.bindData(data[position], typeface)
            Picasso.with(context).load(data[position].photo).into(holder.binding.photo)
        }else if (holder is ContactViewHolder){
            holder.bindData(data[position], typeface)
            Picasso.with(context).load(data[position].photo).into(holder.binding.photo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1)
            return ContactFirstViewHolder(ContactItemFirstBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return ContactViewHolder(ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int) = if (position == 0) 1 else 2
}

class ContactFragment : BaseFragment(){

    @Inject
    @field:SemiBold
    lateinit var typeface: Typeface

    lateinit var recycler_view: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getFragmentComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        (getBaseActivity() as MainActivity).setHeading("Contact")

        recycler_view = view.findViewById(R.id.recycler_view)

        val emailListenerHelper = object : emailOnClickListener{
            override fun onEmailClick(email: String) {
                val TO = arrayOf(email)
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.data = Uri.parse("mailto:")
                emailIntent.type = "text/plain"


                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject")
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here")

                try {
                    activity.startActivity(Intent.createChooser(emailIntent, "Send mail..."))
                    Log.i("Finished email", "")
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(activity,
                            "There is no email client installed.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val phoneListenerHelper = object : phoneOnClickListener {
            override fun onPhoneClick(phoneNo: String) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phoneNo")
                activity.startActivity(intent)
            }
        }

        val phoneListener = View.OnClickListener { v ->
            phoneListenerHelper.onPhoneClick("${(v as TextView).text}")
        }

        val emailListener = View.OnClickListener { v ->
            emailListenerHelper.onEmailClick("${(v as TextView).text}")
        }

        val data = arrayOf(ContactData(R.drawable.pcr, "Alanckrit Jain", "Registration & Other Enquiries", "pcr@bits-apogee.org", "", emailListener, phoneListener),
                ContactData(R.drawable.controls, "Himangshu Baid", "Events, Competitions and operations", "controls@bits-apogee.org", "+91-9704050069", emailListener, phoneListener),
                ContactData(R.drawable.sponz, "Keshav Jain", "Sponsorship and Marketing", "sponsorship@bits-apogee.org", "+91-8320841501", emailListener, phoneListener),
                ContactData(R.drawable.dvm, "Hitesh Raghuvanshi", "Website, App & Online Payments", "webmaster@bits-apogee.org", "+91-8003398809", emailListener, phoneListener),
                ContactData(R.drawable.adp, "Vaibhav Jain", "Art, Design & Publicity", "adp@bits-apogee.org", "+91-8239737593", emailListener, phoneListener),
                ContactData(R.drawable.placeholder, "Anshuman Sharma", "Reception and Accommodation", "recnacc@bits-apogee.org", "+91-9425331555", emailListener, phoneListener),
                ContactData(R.drawable.prez, "Bharatha Ratna Puli", "President, Students Union", "president@pilani.bits-pilani.ac.in", "+91-8297039977", emailListener, phoneListener),
                ContactData(R.drawable.gensec, "Shivam Jindal", "General Secretary, Students Union", "gensec@pilani.bits-pilani.ac.in", "+91-9717024281", emailListener, phoneListener),
                ContactData(R.drawable.placeholder, "Abhishek Gupta", "Paper Presentations and Guest Lectures", "pep@bits-apogee.org", "+91-9453212629", emailListener, phoneListener))

        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = ContactAdapter(data, typeface, activity)

        return view
    }
}