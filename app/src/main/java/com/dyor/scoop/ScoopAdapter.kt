package com.dyor.scoop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScoopAdapter(private val scoopList: ArrayList<Scoop>, val clickListener: (Scoop) -> Unit) : RecyclerView.Adapter<ScoopAdapter.MyViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.scoop_item, parent, false)
    return MyViewHolder(itemView)
  }


  override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    val scoop : Scoop = scoopList[position]
    holder.scoopName.text = scoop.scoopName

    // Calling the clickListener sent by the constructor
    holder?.itemView?.setOnClickListener { clickListener(scoop) }
  }

  override fun getItemCount(): Int {
    return scoopList.size
  }

  public class MyViewHolder(itemView: View) :  RecyclerView.ViewHolder(itemView)
  {
    val scoopName : TextView = itemView.findViewById(R.id.tbScoopName)
  }
}