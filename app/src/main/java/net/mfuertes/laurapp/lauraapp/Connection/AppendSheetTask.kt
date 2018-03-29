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
import com.google.api.services.sheets.v4.model.AppendValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange
import java.util.*


/**
 * Created by hkfuertes on 28-Mar-18.
 */

class AppendSheetTask : SheetTask {
    private val id: String
    private val values: List<String>
    private val page: String

    constructor(credential: GoogleAccountCredential, appname: String, listener: OnFinishListener, id: String, page:String,  values: List<String>) : super(credential,appname, listener) {
        this.id = id
        this.values = values
        this.page = page
    }

    override fun executeCommand(): Spreadsheet? {
        //Sheets.Spreadsheets.Get request = mService.spreadsheets().get("1BjMo4XuTtS64msluVVzepPVQkNxTeACbfdffc6dmJIs");
        //String range = "Class Data!A2:E";

        val items = Arrays.asList(values)
        val body = ValueRange().setValues(items as List<MutableList<Any>>?)
        val request: Sheets.Spreadsheets.Values.Append = mService.spreadsheets().values().append(id, page, body).setValueInputOption("RAW")

        request.execute()
        return null
    }
}