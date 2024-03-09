package com.chordz.eprachar

import android.Manifest
import android.accessibilityservice.AccessibilityService
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.chordz.eprachar.data.MainRepository
import com.chordz.eprachar.data.StoreData.msgDetails
import com.chordz.eprachar.data.remote.RetroFitService.Companion.getInstance
import com.chordz.eprachar.data.response.DataItem
import com.chordz.eprachar.data.response.ElectionMessageResponse
import com.chordz.eprachar.viewModel.AppViewModelFactory
import com.chordz.eprachar.viewModel.HomeViewModel

class MainActivity : AppCompatActivity() {
    var editText = ""
    var apiResponse = ""
    private var phoneNumberEditText: EditText? = null
    private var openWhatsAppButton: Button? = null
    private var homeViewModel: HomeViewModel? = null
    private var detailsList: ArrayList<DataItem?>? = ArrayList()
    private val dataItem: DataItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!isAccessibilityOn(this, WhatsappAccessibilityService::class.java)) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            this.startActivity(intent)
        }

        if (!foregroundServiceRunning()) {
            val serviceIntent = Intent(this, MyForegroundService::class.java)
            startForegroundService(serviceIntent)
        }
        initView()
        fetchData()
        val serviceManager = AccessibilityServiceManager(this)
        if (!serviceManager.hasAccessibilityServicePermission(MyAccessibilityService::class.java)) {
            serviceManager.requestUserForAccessibilityService(this)
        }
    }

    private fun isAccessibilityOn(
        context: Context,
        clazz: Class<out AccessibilityService?>
    ): Boolean {
        var accessibilityEnabled = 0
        val service = context.packageName + "/" + clazz.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                context.applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (ignored: Settings.SettingNotFoundException) {
        }
        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue: String = Settings.Secure.getString(
                context.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                colonSplitter.setString(settingValue)
                while (colonSplitter.hasNext()) {
                    val accessibilityService = colonSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun fetchData() {
//        homeViewModel.getMsg();
    }

    private fun initObservable() {
        homeViewModel!!.electionmsgLiveData.observe(this) { electionMessageResponse: ElectionMessageResponse ->
            if (!electionMessageResponse.data!!.isEmpty()) {
                detailsList = electionMessageResponse.data as ArrayList<DataItem?>?
                msgDetails = electionMessageResponse
            }
        }
    }

    fun foregroundServiceRunning(): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (MyForegroundService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun initView() {
        val retrofitService = getInstance()
        val mainRepository = MainRepository(retrofitService)
        homeViewModel = ViewModelProvider(this, AppViewModelFactory(mainRepository)).get(
            HomeViewModel::class.java
        )
        initObservable()
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        openWhatsAppButton = findViewById(R.id.openWhatsAppButton)
        openWhatsAppButton?.setOnClickListener(View.OnClickListener { check() })


//        AccessibilityServiceManager serviceManager = new AccessibilityServiceManager(this);
//        openWhatsAppButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (serviceManager.hasAccessibilityServicePermission(MyAccessibilityService.class)) {
//                    String message = "Your message", to = "917972546880";
//                    startActivity(
//                            new Intent(Intent.ACTION_VIEW,
//                                    Uri.parse(
//                                            String.format("https://api.whatsapp.com/send?phone=%s&text=%s", to, message)
//                                    )
//                            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    );
//                } else {
//                    serviceManager.requestUserForAccessibilityService(MainActivity.this);
//                }
//            }
//        });
        if (checkPermissions()) {
            // Permissions are already granted, proceed with your logic
        } else {
            // Request permissions
            requestPermissions()
        }
        val intent = Intent(this, MyBackgroundService::class.java)
        startService(intent)
    }

    override fun onResume() {
        super.onResume()
        check()
    }

    private fun check() {
        val editTextValue = phoneNumberEditText!!.text.toString().trim { it <= ' ' }
        if (editTextValue.isEmpty()) {
            Toast.makeText(this, "Please enter admin number", Toast.LENGTH_SHORT).show()
            return
        }
        try {
//            int messageId = Integer.parseInt(editTextValue);
//            homeViewModel.getMsgById(messageId);
            homeViewModel!!.getMsgContactNo(editTextValue)
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        } catch (e: NumberFormatException) {
            // Handle the case when the input is not a valid integer
            showMismatchAlertDialog()
        }
    }

    private fun showMismatchAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Path Parameter Mismatch")
        builder.setMessage("The input does not match the expected path parameter.")
        builder.setPositiveButton("OK") { dialog: DialogInterface?, which: Int -> }
        builder.setCancelable(false)
        builder.show()
    }

    private fun checkPermissions(): Boolean {
        val readCallLogPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
        val readPhoneStatePermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        val readOutgoingCallPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS)
        val sendSmsPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
        val receiveSmsPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
        return readCallLogPermission == PackageManager.PERMISSION_GRANTED && readPhoneStatePermission == PackageManager.PERMISSION_GRANTED && readOutgoingCallPermission == PackageManager.PERMISSION_GRANTED && sendSmsPermission == PackageManager.PERMISSION_GRANTED && receiveSmsPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            var allPermissionsGranted = true
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false
                    break
                }
            }
            if (allPermissionsGranted) {
                // All permissions are granted, proceed with your logic
            } else {
                // Permissions are not granted, show an alert dialog
                showPermissionAlertDialog()
            }
        }
    }

    private fun showPermissionAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permission Required")
        builder.setMessage("Please allow all the required permissions to use this app.")
        builder.setPositiveButton("OK") { dialog: DialogInterface?, which: Int ->
            // Request permissions again
            requestPermissions()
        }
        builder.setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int ->
            // Handle the case when the user cancels the permission request
            // You can choose to finish the activity or take appropriate action
            finish()
        }
        builder.setCancelable(false)
        builder.show()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}