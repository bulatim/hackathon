package com.hack.qrpay.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import me.dm7.barcodescanner.zxing.ZXingScannerView
import android.os.Bundle
import com.google.zxing.Result


class SimpleScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var mScannerView: ZXingScannerView

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)
    }

    public override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    public override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()           // Stop camera on pause
    }

    override fun handleResult(rawResult: Result?) {
        val intent = Intent()
        intent.putExtra("result", rawResult.toString())
        setResult(RESULT_OK, intent)
        finish()
    }

}