package com.hack.qrpay.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hack.qrpay.R

class ServiceAdapter constructor(private val services: List<String>,
                                 private val clickService: OnClickService)
    : RecyclerView.Adapter<ServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.service_item_row, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val repository = services[position]
        holder.bind(repository, clickService)
    }

    override fun getItemCount(): Int = services.size

}
