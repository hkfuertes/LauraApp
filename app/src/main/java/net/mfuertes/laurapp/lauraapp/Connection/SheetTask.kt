package net.mfuertes.laurapp.lauraapp.Connection

import android.os.AsyncTask
import android.util.Log
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.services.sheets.v4.model.Spreadsheet

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.SpreadsheetProperties
import java.io.IOException

/**
 * Created by hkfuertes on 28-Mar-18.
 */

abstract class SheetTask : AsyncTask<Void, Void, Spreadsheet> {

    interface OnFinishListener{
        fun onFinish(sheet: Spreadsheet?)
    }

    var mService: com.google.api.services.sheets.v4.Sheets
    var mLastError: Exception? = null

    val listener : OnFinishListener?

    constructor(credential: GoogleAccountCredential, appname: String, listener: OnFinishListener) {
        //Log.d("TEST", credential.selectedAccountName)
        this.listener = listener
        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()
        mService = Sheets.Builder(
                transport, jsonFactory, credential)
                .setApplicationName(appname)
                .build()
    }



    override fun doInBackground(vararg params: Void): Spreadsheet? {
        try {
            return executeCommand()
        } catch (e: Exception) {
            mLastError = e
            e.printStackTrace()
            cancel(true)
            return null
        }

    }

    override fun onPostExecute(result: Spreadsheet?) {
        super.onPostExecute(result)
        listener?.onFinish(result)
    }

    @Throws(IOException::class)
    abstract fun executeCommand(): Spreadsheet?

}