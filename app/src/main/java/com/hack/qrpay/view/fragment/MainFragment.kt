package com.hack.qrpay.view.fragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hack.qrpay.MainApplication
import com.hack.qrpay.R
import com.hack.qrpay.view.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.regex.Pattern
import javax.inject.Inject


class MainFragment : Fragment() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MainApplication).component
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registration.setOnClickListener {
            if (phone_number.text.toString() == "") {
                phone_number.error = "Необходимо заполнить номер телефона"
                return@setOnClickListener
            } else phone_number.error = null
            if (card_number.text.toString() == "") {
                card_number.error = "Необходимо заполнить номер карты"
                return@setOnClickListener
            } else card_number.error = null
            if (!isMatchPattern("^[+78][0-9]{10,11}",
                            phone_number.text.toString().replace("[ ()-]".toRegex(),
                                    ""))) {
                phone_number.error = "Не корретный номер телефона"
                return@setOnClickListener
            }
            if (!isMatchPattern("[0-9]{16,19}",
                            card_number.text.toString().replace("[ ()-]".toRegex(),
                                    ""))) {
                phone_number.error = "Не корретный номер карты"
                return@setOnClickListener
            }
            if (phone_number.error == null && card_number.error == null) {
                sharedPreferences.edit()
                        .putString("phoneNumber", phone_number.text.toString().replace("[ ()-]".toRegex(),""))
                        .putString("cardNumber", card_number.text.toString().replace("[ ()-]".toRegex(),""))
                        .apply()
                (activity as MainActivity).changeFragment(ServiceFragment(), false)
            } else
                Toast.makeText(context, "Не все поля заполнены", Toast.LENGTH_LONG).show()
        }
    }

    fun isMatchPattern(regex: String, text: String): Boolean {
        val pattern = Pattern.compile(regex)
        return pattern.matcher(text).find()
    }
}
