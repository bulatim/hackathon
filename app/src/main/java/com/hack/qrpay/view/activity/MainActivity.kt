package com.hack.qrpay.view.activity

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hack.qrpay.MainApplication
import com.hack.qrpay.R
import com.hack.qrpay.view.fragment.MainFragment
import com.hack.qrpay.view.fragment.QrGenerateFragment
import com.hack.qrpay.view.fragment.ScanResultFragment
import com.hack.qrpay.view.fragment.ServiceFragment
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    lateinit var phoneNumber: String
    private val CAMERA_SMS_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MainApplication).component
                .inject(this)

        setContentView(R.layout.activity_main)
        initialFragment()
        setupPermissions()
    }

    fun setupPermissions() {
        var permission = 0
        for (permissionItem in arrayOf(Manifest.permission.CAMERA,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS)) {
            permission = ContextCompat.checkSelfPermission(this, permissionItem)
            if (permission != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.CAMERA) ) {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Для этого приложения требуется предоставить разрешения")
                            .setTitle("Требуется разрешение")
                    builder.setPositiveButton("OK"
                    ) { _, _ ->
                        makeRequest()
                    }

                    val dialog = builder.create()
                    dialog.show()
                } else {
                    makeRequest()
                }
            }
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS),
                CAMERA_SMS_REQUEST_CODE)
    }

    private fun initialFragment() {
        phoneNumber = sharedPreferences.getString("phoneNumber", "")!!
        val cardNumber = sharedPreferences.getString("cardNumber", "")
        supportFragmentManager.beginTransaction()
                .replace(R.id.contentContainer, if (phoneNumber == "" || cardNumber == "") MainFragment() else ServiceFragment())
                .commit()
    }

    fun changeFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val fragmentTransaction = supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentContainer, fragment)
        if (addToBackStack)
            fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (data == null) {
                return
            }
            val result = data.getStringExtra("result")
            if (result.contains("phone") && result.contains("amount")) {
                val phoneAmountArray = result.split("|")
                if (phoneAmountArray.size == 2 && phoneAmountArray[0].split("=").size == 2 &&
                        phoneAmountArray[1].split("=").size == 2)
                    changeFragment(ScanResultFragment.newInstance(phoneAmountArray[0].split("=")[1],
                            phoneAmountArray[1].split("=")[1]))
            }
        }
    }

    fun onClickRepoList(service: String) {
        when (service) {
            "Отправить по QR" -> {
                val intent = Intent(this, SimpleScannerActivity::class.java)
                startActivityForResult(intent, 100)
            }
            "Получить" -> {
                changeFragment(QrGenerateFragment.newInstance(""), false)
            }
            "Отправить по № тел" -> {
                changeFragment(ScanResultFragment.newInstance(),false)
            }
            else -> Toast.makeText(this, service, Toast.LENGTH_SHORT).show()
        }
    }
}
