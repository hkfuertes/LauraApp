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

class CreateSheetTask : SheetTask {

    private val titulo: String

    constructor(credential: GoogleAccountCredential, appname: String, titulo: String, listener: OnFinishListener) : super(credential, appname, listener) {
        this.titulo = titulo
    }

    override fun executeCommand(): Spreadsheet {
        val requestBody = Spreadsheet()
        val properties = SpreadsheetProperties()
        properties.title = titulo
        requestBody.properties = properties


        //La idea va a ser, crear una y guardarme el id.
        val request: Sheets.Spreadsheets.Create = mService.spreadsheets().create(requestBody)

        return request.execute()
    }

}