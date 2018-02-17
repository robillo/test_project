package com.robillo.test_project.view.main.list_fragment.list_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.robillo.test_project.R
import com.robillo.test_project.network.model.AllDetails
import com.robillo.test_project.view.main.MainActivity
import com.robillo.test_project.view.main.list_fragment.ListFragment

/**
 * Created by robinkamboj on 17/02/18.
 */

internal class WorldListAdapter(private val mList: List<AllDetails.WorldpopulationBean>?, private val mContext: Context) : RecyclerView.Adapter<WorldListAdapter.WorldHolder>() {
    private var parentContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorldHolder {
        parentContext = parent.context
        return WorldHolder(LayoutInflater.from(parentContext).inflate(R.layout.row_list_adapter, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WorldHolder, position: Int) {

        holder.rank.text = "Rank: " + Integer.toString(mList!![position].rank)
        holder.country.text = "Country: " + mList[position].country
        holder.population.text = "Population: " + mList[position].population
        Glide.with(parentContext).load(mList[position].flag).fitCenter().into(holder.flag)

        holder.itemView.setOnClickListener { (mContext as MainActivity).addFullScreenFragment(mList[position].flag) }
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    internal inner class WorldHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var flag: ImageView
        var rank: TextView
        var country: TextView
        var population: TextView

        init {
            flag = itemView.findViewById(R.id.flag)
            rank = itemView.findViewById(R.id.rank)
            country = itemView.findViewById(R.id.country)
            population = itemView.findViewById(R.id.population)
        }
    }
}
