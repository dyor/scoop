package com.dyor.scoop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dyor.scoop.R

class JobAdapter(private val jobList: ArrayList<Job>, val clickListener: (Job) -> Unit) : RecyclerView.Adapter<JobAdapter.MyViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.job_item, parent, false)
    return MyViewHolder(itemView)
  }

  override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    val job : Job = jobList[position]
    holder.jobName.text = job.jobName

    // Calling the clickListener sent by the constructor
    holder?.itemView?.setOnClickListener { clickListener(job) }
  }

  override fun getItemCount(): Int {
    return jobList.size
  }

  public class MyViewHolder(itemView: View) :  RecyclerView.ViewHolder(itemView)
  {
    val jobName : TextView = itemView.findViewById(R.id.tbJobName)
  }
}