package com.hack.qrpay.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage


class IncomingSMSReceiver : BroadcastReceiver() {
    val pdu_type = "pdus"

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let { it ->
            val bundle = it.extras
            bundle?.let {
                val format = bundle.getString("format")
                val pdus = bundle.get(pdu_type) as Array<Any>
                val msgs = arrayOfNulls<SmsMessage>(pdus.size)
                var message = ""
                for (i in msgs.indices) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray, format)
                    } else {
                        msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                    }
                    if (msgs[i]?.originatingAddress == "900") {
                        message += msgs[i]?.messageBody
                    }
                }
                val broadCastIntent = Intent("bankMessageReceiver")
                broadCastIntent.putExtra("message", message)
                context?.sendBroadcast(broadCastIntent)
            }
        }
    }
}