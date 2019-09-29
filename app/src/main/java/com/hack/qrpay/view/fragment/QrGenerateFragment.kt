package com.hack.qrpay.view.fragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hack.qrpay.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import android.graphics.Bitmap
import com.google.zxing.common.BitMatrix
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import com.bumptech.glide.Glide
import com.hack.qrpay.MainApplication
import kotlinx.android.synthetic.main.fragment_qr_generate.*
import javax.inject.Inject


class QrGenerateFragment : Fragment() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    lateinit var mPhone: String
    lateinit var mAmount: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MainApplication).component
                .inject(this)
    }

    companion object {
        fun newInstance(amount: String): QrGenerateFragment {
            val fragment = QrGenerateFragment()
            fragment.mAmount = amount
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_qr_generate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPhone = sharedPreferences.getString("phoneNumber", "")!!
        phone_number.setText(mPhone)
        amount.setText(mAmount)
        amount.isEnabled = amount.text.toString() == "" || amount.text.toString() == "0"
        if (amount.text.toString() != "" && amount.text.toString() != "0")
            generateQr()
        submit.setOnClickListener {
            amount.setText(amount.text.toString().trim())
            if (amount.error == null) {
                mAmount = amount.text.toString()
                generateQr()
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

    fun generateQr() {
        val bitMatrix = QRCodeWriter().encode("phone=$mPhone|amount=$mAmount",
                BarcodeFormat.QR_CODE, 200, 200)
        val bitmap = generateQRBitmap(bitMatrix)
        Glide.with(context!!)
                .asBitmap()
                .load(bitmap)
                .into(qr)
    }

    fun generateQRBitmap(matrix: BitMatrix): Bitmap {
        val w = matrix.width
        val h = matrix.height
        val rawData = IntArray(w * h)
        for (i in 0 until w) {
            for (j in 0 until h) {
                var color = Color.WHITE
                if (matrix.get(i, j)) {
                    color = Color.BLACK
                }
                rawData[i + j * w] = color
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565)
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h)
        return bitmap
    }
}
