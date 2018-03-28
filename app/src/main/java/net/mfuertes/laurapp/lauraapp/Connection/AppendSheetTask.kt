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

class AppendSheetTask : SheetTask {
    private val id: String

    constructor(credential: GoogleAccountCredential, appname: String, listener: OnFinishListener, id: String) : super(credential,appname, listener) {
        this.id = id
    }

    override fun executeCommand(): Spreadsheet {
        var request: Sheets.Spreadsheets.Get = mService.spreadsheets().get(id);
        return request.execute()
    }
}