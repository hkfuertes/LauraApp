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

class AppendSheetTask : AsyncTask<Void, Void, Spreadsheet> {
    private var mService: com.google.api.services.sheets.v4.Sheets
    private var mLastError: Exception? = null
    private val id: String

    constructor(credential: GoogleAccountCredential, appname: String, id: String) {
        this.id = id
        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()
        mService = Sheets.Builder(
                transport, jsonFactory, credential)
                .setApplicationName(appname)
                .build()
    }



    override fun doInBackground(vararg params: Void): Spreadsheet? {
        try {
            return getDataFromApi()
        } catch (e: Exception) {
            mLastError = e
            cancel(true)
            return null
        }

    }

    @Throws(IOException::class)
    private fun getDataFromApi(): Spreadsheet {

        var request: Sheets.Spreadsheets.Get = mService.spreadsheets().get(id);

        return request.execute()
    }

}