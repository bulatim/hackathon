package com.hack.qrpay.view.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hack.qrpay.R
import com.hack.qrpay.view.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_confirm.*
import java.util.regex.Pattern


class ConfirmResultFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.registerReceiver(bankMessageReceiver, IntentFilter("bankMessageReceiver"))
        submit.setOnClickListener {
            try {
                SmsManager.getDefault()
                        .sendTextMessage("900", null,
                                code.text.toString(),
                                null,
                                null)
                Toast.makeText(context, "Сообщение отправлено", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Произошла ошибка при отправке сообщения", Toast.LENGTH_LONG).show()
            }
        }
    }

    private var bankMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val b = intent.extras
            val message = b?.getString("message")
            message?.let {
                if (it.contains("отправьте код") && it.contains("Для перевода") && it.contains("получателю")) {
                    val codeTmp = getSubstringByRegex("отправьте код \\d+", it)
                            .replace("отправьте код ", "")
                    code.setText(codeTmp)

                    val amountTmp = getSubstringByRegex("Для перевода \\d{1,5}р", it)
                            .replace("Для перевода ", "").replace("р", "")
                    amount.setText(amountTmp)

                    val recipientTmp = getSubstringByRegex("получателю \\D+ на", it)
                            .replace("получателю ", "").replace(" на", "")
                    recepient_name.setText(recipientTmp)
                } else if (it.contains("перевод ${amount.text.toString()}р") && it.contains("Баланс: ")) {
                    (activity as MainActivity).changeFragment(PayStatusFragment.newInstance("Успешно"), false)
                } else {
                    (activity as MainActivity).changeFragment(PayStatusFragment.newInstance("Ошибка", it), false)
                }
            }
        }

        fun getSubstringByRegex(regex: String, text: String): String {
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(text);
            var textSubstring = ""
            while (matcher.find()) {
                textSubstring += text.substring(matcher.start(), matcher.end())
            }
            return textSubstring
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(bankMessageReceiver)
    }
}
