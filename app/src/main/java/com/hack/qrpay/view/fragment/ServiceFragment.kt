package com.hack.qrpay.view.fragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hack.qrpay.MainApplication
import com.hack.qrpay.R
import com.hack.qrpay.adapter.OnClickService
import com.hack.qrpay.adapter.ServiceAdapter
import com.hack.qrpay.view.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_services.*
import javax.inject.Inject


class ServiceFragment : Fragment(), OnClickService {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var serviceAdapter: ServiceAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MainApplication).component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val phoneNumber = sharedPreferences.getString("phoneNumber", "")
        phone_number.text = phoneNumber
        configurationRecyclerView()
    }

    private fun configurationRecyclerView() {
        rvList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
        }

        val services = ArrayList<String>()
//        services.add("Номер телефона")
        services.add("Отправить по QR")
        services.add("Отправить по № тел")
//        services.add("Отправить контакту")
        services.add("Получить")
//        services.add("Выйти")
        serviceAdapter = ServiceAdapter(services, this)
        serviceAdapter.notifyDataSetChanged()
        rvList.adapter = serviceAdapter
    }

    override fun onClickRepoItem(service: String) {
        (activity as MainActivity).onClickRepoList(service)
    }
}
