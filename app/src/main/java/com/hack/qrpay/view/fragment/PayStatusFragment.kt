package com.hack.qrpay.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hack.qrpay.R
import com.hack.qrpay.view.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_pay_status.*


class PayStatusFragment : Fragment() {
    lateinit var status: String
    lateinit var error: String

    companion object {
        fun newInstance(status: String, error: String = ""): PayStatusFragment {
            val fragment = PayStatusFragment()
            fragment.status = status
            fragment.error = error
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pay_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statusTv.text = status
        errorTv.text = error
        if (error != "")
            errorTv.visibility = View.VISIBLE
        submit.setOnClickListener {
            (activity as MainActivity).changeFragment(ServiceFragment(), false)
        }
    }
}
