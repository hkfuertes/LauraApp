package net.mfuertes.laurapp.lauraapp.Connection

import android.content.Context
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.*
import net.mfuertes.laurapp.lauraapp.R
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by hkfuertes on 28-Mar-18.
 */

class CreateSheetTask : SheetTask {

    private val titulo: String
    private var context: Context

    constructor(credential: GoogleAccountCredential, appname: String, listener: OnFinishListener, titulo: String, context: Context) : super(credential, appname, listener) {
        this.titulo = titulo
        this.context = context
    }

    override fun executeCommand(): Spreadsheet {
        val planta_sheet_props = SheetProperties().setTitle(context.getString(R.string.title_planta)).setGridProperties(GridProperties().setFrozenRowCount(1))
        val ic_sheet_props = SheetProperties().setTitle(context.getString(R.string.title_ptes_ic)).setGridProperties(GridProperties().setFrozenRowCount(1))
        val sesion_sheet_props = SheetProperties().setTitle(context.getString(R.string.title_sesion)).setGridProperties(GridProperties().setFrozenRowCount(1))
        val guardia_sheet_props = SheetProperties().setTitle(context.getString(R.string.title_guardias)).setGridProperties(GridProperties().setFrozenRowCount(1))

        //Creamos el fichero
        val requestBody = Spreadsheet().setProperties(SpreadsheetProperties().setTitle(this.titulo))
        val create_request: Sheets.Spreadsheets.Create = mService.spreadsheets().create(requestBody)
        val result = create_request.execute()

        //Anadimos las hojas
        val batchUpdateSpreadsheetRequest = BatchUpdateSpreadsheetRequest()
        val requests = ArrayList<Request>()

        requests.add(0, Request().setAddSheet(AddSheetRequest().setProperties(planta_sheet_props)))
        requests.add(1, Request().setAddSheet(AddSheetRequest().setProperties(ic_sheet_props)))
        requests.add(2, Request().setAddSheet(AddSheetRequest().setProperties(sesion_sheet_props)))
        requests.add(3, Request().setAddSheet(AddSheetRequest().setProperties(guardia_sheet_props)))

        //Eliminar la primera
        requests.add(4, Request().setDeleteSheet(DeleteSheetRequest().setSheetId(result.sheets.get(0).properties.sheetId)))

        //Estilo para interconsulta
        val formatRequest = RepeatCellRequest()
                .setRange(GridRange().setSheetId(ic_sheet_props.sheetId).setStartRowIndex(0).setEndRowIndex(1))
                .setCell(CellData().setUserEnteredFormat(CellFormat().setTextFormat(TextFormat().setBold(true)).setHorizontalAlignment("CENTER")))
                .setFields("userEnteredFormat(textFormat,horizontalAlignment)")

        //Formato a interconsulta
        requests.add(4,Request().setRepeatCell(formatRequest))
        batchUpdateSpreadsheetRequest.requests = requests

        mService.spreadsheets().batchUpdate(result.spreadsheetId, batchUpdateSpreadsheetRequest).execute()

        //For Interconsulta
        val ic_body = ValueRange().setValues(Arrays.asList(context.resources.getStringArray(R.array.ptes_ic)) as List<MutableList<Any>>?)
        mService.spreadsheets().values().append(result.spreadsheetId, ic_sheet_props.title, ic_body).setValueInputOption("RAW").execute()

        //For Planta
        val planta_body = ValueRange().setValues(Arrays.asList(context.resources.getStringArray(R.array.planta)) as List<MutableList<Any>>?)
        mService.spreadsheets().values().append(result.spreadsheetId, planta_sheet_props.title, planta_body).setValueInputOption("RAW").execute()

        //For Guardia
        val guardia_body = ValueRange().setValues(Arrays.asList(context.resources.getStringArray(R.array.sesion)) as List<MutableList<Any>>?)
        mService.spreadsheets().values().append(result.spreadsheetId, guardia_sheet_props.title, guardia_body).setValueInputOption("RAW").execute()

        //For Sesion
        val sesion_body = ValueRange().setValues(Arrays.asList(context.resources.getStringArray(R.array.guardias)) as List<MutableList<Any>>?)
        mService.spreadsheets().values().append(result.spreadsheetId, sesion_sheet_props.title, sesion_body).setValueInputOption("RAW").execute()

        return result
    }

}