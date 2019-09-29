package com.hack.qrpay.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View


import kotlinx.android.synthetic.main.service_item_row.view.*

class ServiceViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(service: String, clickService: OnClickService) {
        view.name.text = service
        view.body.setOnClickListener {
            clickService.onClickRepoItem(service)
        }
    }
}
