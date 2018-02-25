package org.dvm.bits_apogee.ui.informatives

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import org.dvm.bits_apogee.R
import org.dvm.bits_apogee.data.dataManager
import org.dvm.bits_apogee.data.firebase.model.Sponsor
import org.dvm.bits_apogee.databinding.SponsorsItemBinding
import org.dvm.bits_apogee.ui.base.BaseFragment

class SponsorsViewHolder(val binding: SponsorsItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bindData(data: Sponsor, typeface: Typeface){
        binding.data = data
        binding.typeface = typeface
    }
}

class SponsorsAdapter(val typeface: Typeface, val context: Context, val sponsors: List<Sponsor>) : RecyclerView.Adapter<SponsorsViewHolder>(){

    override fun onBindViewHolder(holder: SponsorsViewHolder, position: Int) {
        holder.bindData(sponsors[position], typeface)
        try {
            Picasso.with(context).load(sponsors[position].imageURL).fit().centerInside().into(holder.binding.photo)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SponsorsViewHolder {
        return SponsorsViewHolder(SponsorsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = sponsors.size
}

class SponsorsFragment : BaseFragment(){

    lateinit var typeface: Typeface
    lateinit var recyclerView: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_sponsors, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        typeface = Typeface.DEFAULT_BOLD
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        dataManager.getDataManager().getSponsors { recyclerView.adapter = SponsorsAdapter(typeface, activity, it) }
    }
}