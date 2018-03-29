package net.mfuertes.laurapp.lauraapp

import android.Manifest
import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.preference.EditTextPreference
import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.util.Log
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

        private lateinit var editor: SharedPreferences.Editor
        private val SHEET_ID = "sheet_id"
        private val SHEET_URL = "sheet_url"
        private val SHEET_NAME = "sheet_name"
        private val CONNECTED = "connection_token"

        override fun onFinish(sheet: Spreadsheet?) {
            Toast.makeText(activity,sheet?.spreadsheetUrl, Toast.LENGTH_LONG).show()

            editor.putString(SHEET_URL,sheet?.spreadsheetUrl)
            editor.putString(SHEET_ID,sheet?.spreadsheetId)
            editor.commit()

            val url = this.findPreference(SHEET_URL) as Preference
            url.setSummary(sheet?.spreadsheetUrl)
            url.setEnabled(true)

            val name = this.findPreference(SHEET_NAME) as Preference
            name.setEnabled(false)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences)

            val ssact  = activity as SingleSettingsActivity

            editor = preferenceManager.sharedPreferences.edit()

            val url = this.findPreference(SHEET_URL) as Preference
            val connect = this.findPreference(CONNECTED) as SwitchPreference
            val name = this.findPreference(SHEET_NAME) as EditTextPreference


            url.setSummary(preferenceManager.sharedPreferences.getString(url.key,"sheet_id"))
            url.setEnabled(preferenceManager.sharedPreferences.getString(url.key,"sheet_id").equals("").not())
            name.setEnabled(preferenceManager.sharedPreferences.getBoolean(connect.key,true))
            name.setSummary(preferenceManager.sharedPreferences.getString(name.key,ssact.APP_NAME))

            connect.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference: Preference, any: Any ->

                val selected: Boolean = any as Boolean

                if(selected){
                    //On Selected ask for credentials
                    ssact.getAddCredentials()
                    name.setEnabled(true)

                    if(name.text.equals("").not()){
                        //Recuperar el id si existe y sino crear
                    }

                    editor.putBoolean("connection_token",true)
                    editor.commit()
                }else{
                    name.setEnabled(false)

                    url.summary = ""
                    url.setEnabled(false)

                    editor.putString("sheet_id","")
                    editor.commit()
                }

                true
            }


            name.onPreferenceChangeListener = Preference.OnPreferenceChangeListener() { preference: Preference, any: Any ->

                name.summary = any.toString()

                editor.putString("sheet_name",any.toString())
                editor.commit()

                CreateSheetTask(ssact.mCredential, ssact.APP_NAME, any.toString(), this).execute()

                false
            }

            url.onPreferenceClickListener = Preference.OnPreferenceClickListener { preference: Preference ->

                //Open Browser

                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url.summary.toString()))
                ssact.startActivity(browserIntent)

                false
            }

        }
    }

    fun getAddCredentials(): Boolean {
        if (mCredential.selectedAccountName == null) {
            chooseAccount()
            return false
        } else if (!isDeviceOnline()) {
            Toast.makeText(this,NO_CONNECTION,Toast.LENGTH_LONG).show()
            return false
        }else {
            return true
        }
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