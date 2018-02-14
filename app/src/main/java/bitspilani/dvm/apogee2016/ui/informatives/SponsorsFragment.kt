package bitspilani.dvm.apogee2016.ui.informatives

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bitspilani.dvm.apogee2016.R
import bitspilani.dvm.apogee2016.databinding.SponsorsItemBinding
import bitspilani.dvm.apogee2016.di.SemiBold
import bitspilani.dvm.apogee2016.ui.base.BaseActivity
import bitspilani.dvm.apogee2016.ui.base.BaseFragment
import bitspilani.dvm.apogee2016.ui.main.MainActivity
import com.squareup.picasso.Picasso
import javax.inject.Inject

data class SponsorsData(val imageUrl: String, val type: String, val name: String, val priority: String)

class SponsorsViewHolder(val binding: SponsorsItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bindData(data: SponsorsData, typeface: Typeface){
        binding.data = data
        binding.typeface = typeface
    }
}

class SponsorsAdapter(val typeface: Typeface, val context: Context) : RecyclerView.Adapter<SponsorsViewHolder>(){
    override fun onBindViewHolder(holder: SponsorsViewHolder, position: Int) {
        TODO("holder.bindData(GlobalData.sponsorsData[position], typeface)")
        try {
            TODO("Picasso.with(context).load(GlobalData.sponsorsData[position].imageUrl).fit().centerInside().into(holder.binding.photo)")
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SponsorsViewHolder {
        return SponsorsViewHolder(SponsorsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = TODO("GlobalData.sponsorsData.size")
}

class SponsorsFragment : BaseFragment(){

    @Inject
    @field:SemiBold
    lateinit var typeface: Typeface

    lateinit var recyclerView: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getFragmentComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_sponsors, container, false)
        (getBaseActivity() as MainActivity).setHeading("Sponsors")
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        recyclerView.adapter = SponsorsAdapter(typeface, activity)
        return view
    }
}