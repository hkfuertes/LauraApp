package net.mfuertes.laurapp.lauraapp

import android.Manifest
import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.EditTextPreference
import android.preference.Preference.OnPreferenceClickListener
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.widget.Toast
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.Spreadsheet
import net.mfuertes.laurapp.lauraapp.Connection.CreateSheetTask
import net.mfuertes.laurapp.lauraapp.Connection.SheetTask.OnFinishListener
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


class SingleSettingsActivity : AppCompatPreferenceActivity(), EasyPermissions.PermissionCallbacks {

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private lateinit var mCredential: GoogleAccountCredential
    private val SCOPES = arrayOf(SheetsScopes.DRIVE, SheetsScopes.SPREADSHEETS)

    internal val REQUEST_ACCOUNT_PICKER = 1000
    internal val REQUEST_AUTHORIZATION = 1001
    internal val REQUEST_GOOGLE_PLAY_SERVICES = 1002
    internal val REQUEST_PERMISSION_GET_ACCOUNTS = 1003

    private val NO_PLAY_SERVICES = "This app requires Google Play Services. Please install " + "Google Play Services on your device and relaunch this app."
    private val NO_CONNECTION = "No network connection available."
    private val PREF_ACCOUNT_NAME = "accountName"
    private val PERMISSION_REQUEST = "This app needs to access your Google account (via Contacts)."
    private val APP_NAME = "FundacionApp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(android.R.id.content, MyPreferenceFragment()).commit()

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(applicationContext, Arrays.asList(*SCOPES)).setBackOff(ExponentialBackOff())
    }

    class MyPreferenceFragment : PreferenceFragment(), OnFinishListener {
        override fun onFinish(sheet: Spreadsheet?) {
            Toast.makeText(activity,sheet?.spreadsheetUrl, Toast.LENGTH_LONG).show()

            val url = this.findPreference("sheet_id") as EditTextPreference
            url.setSummary(sheet?.spreadsheetId)

            url.text = sheet?.spreadsheetId
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences)

            val ssact  = activity as SingleSettingsActivity

            val url = this.findPreference("sheet_id") as EditTextPreference
            url.setSummary(preferenceManager.sharedPreferences.getString(url.key,"sheet_id"))

            val connect = this.findPreference("connection_token") as SwitchPreference
            connect.onPreferenceClickListener = OnPreferenceClickListener { preference ->

                if(connect.isChecked && false){
                    //Remove Credentials and file
                }else{
                    if(ssact.getAddCredentials()){
                        CreateSheetTask(ssact.mCredential, ssact.APP_NAME, preferenceManager.sharedPreferences.getString("sheet_name","name"), this).execute()
                    }
                }
                false
            }

        }
    }

    protected fun getAddCredentials() : Boolean {
        if (mCredential.selectedAccountName == null) {
            chooseAccount()
        } else if (!isDeviceOnline()) {
            Toast.makeText(this,NO_CONNECTION,Toast.LENGTH_LONG).show()
        }else return true
        return false
    }

    private fun chooseAccount() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.GET_ACCOUNTS)) {
            val accountName = getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null)
            if (accountName != null) {
                mCredential.selectedAccountName = accountName
                getAddCredentials()
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER)
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(this, PERMISSION_REQUEST, REQUEST_PERMISSION_GET_ACCOUNTS, Manifest.permission.GET_ACCOUNTS)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_ACCOUNT_PICKER ->
                if (resultCode == Activity.RESULT_OK && data != null && data.extras != null) {
                    val accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                    if (accountName != null) {
                        val settings = getPreferences(Context.MODE_PRIVATE)
                        val editor = settings.edit()
                        editor.putString(PREF_ACCOUNT_NAME, accountName)
                        editor.apply()
                        mCredential.selectedAccountName = accountName
                        getAddCredentials()
                    }
                }
            REQUEST_AUTHORIZATION ->
                if (resultCode == Activity.RESULT_OK) {
                    getAddCredentials()
                }
        }
    }

    private fun isDeviceOnline(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}