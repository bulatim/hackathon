package com.hack.qrpay.view.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hack.qrpay.R
import kotlinx.android.synthetic.main.fragment_scan_result.*
import android.content.Intent
import android.net.Uri
import android.telephony.SmsManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.hack.qrpay.view.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_qr_generate.*
import kotlinx.android.synthetic.main.fragment_scan_result.amount
import kotlinx.android.synthetic.main.fragment_scan_result.phone_number
import kotlinx.android.synthetic.main.fragment_scan_result.submit
import java.util.regex.Pattern


class ScanResultFragment : Fragment() {
    private var mPhone = ""
    private var mAmount: String = ""

    companion object {
        fun newInstance(phone: String, amount: String): ScanResultFragment {
            val fragment = ScanResultFragment()
            fragment.mPhone = phone
            fragment.mAmount = amount
            return fragment
        }

        fun newInstance(): ScanResultFragment {
            val fragment = ScanResultFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scan_result, container, false)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setupPermissions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        phone_number.setText(mPhone)
        phone_number.isEnabled = mPhone == ""
        amount.setText(mAmount)
        amount.isEnabled = mAmount == "" || mAmount == "0"
        submit.setOnClickListener {
            if (phone_number.text.toString() != "" && amount.text.toString() != "") {
                mPhone = phone_number.text.toString()
                amount.setText(amount.text.toString().trim())
                if (!isMatchPattern("^[+78][0-9]{10,11}",
                                phone_number.text.toString().replace("[ ()-]".toRegex(),
                                        ""))) {
                    phone_number.error = "Не корретный номер телефона"
                    return@setOnClickListener
                }
                if (amount.error == null) {
                    try {
                        SmsManager.getDefault()
                                .sendTextMessage("900", null,
                                        "Перевод $mPhone $mAmount",
                                        null,
                                        null)
                        Toast.makeText(context, "Сообщение отправлено", Toast.LENGTH_LONG).show()
                        (activity as MainActivity).changeFragment(ConfirmResultFragment(), false)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Произошла ошибка при отправке сообщения", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        amount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().toInt() > 10000 || p0.toString().toInt() <= 0 || p0.toString() == "") {
                    amount.error = "Не корректная сумма"
                } else amount.error = null
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

    fun isMatchPattern(regex: String, text: String): Boolean {
        val pattern = Pattern.compile(regex)
        return pattern.matcher(text).find()
    }
}
