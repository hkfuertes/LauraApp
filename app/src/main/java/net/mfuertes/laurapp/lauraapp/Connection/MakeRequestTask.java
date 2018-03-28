package net.mfuertes.laurapp.lauraapp.Connection;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;

import java.io.IOException;

public class MakeRequestTask extends AsyncTask<Void, Void, Spreadsheet> {
    private com.google.api.services.sheets.v4.Sheets mService = null;
    private Exception mLastError = null;


    public MakeRequestTask(GoogleAccountCredential credential) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new Sheets.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("FundationApp")
                .build();
    }

    /**
     * Background task to call Google Sheets API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected Spreadsheet doInBackground(Void... params) {
        try {
            Spreadsheet result = getDataFromApi();
            Log.d("REQUEST",result.getSpreadsheetUrl() + "    " + result.getSpreadsheetId());
            return result;
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Fetch a list of names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     *
     * @return List of names and majors
     * @throws IOException
     */
    private Spreadsheet getDataFromApi() throws IOException {

        Spreadsheet requestBody = new Spreadsheet();
        SpreadsheetProperties properties = new SpreadsheetProperties();
        String title = "Titulo";
        properties.setTitle(title);
        requestBody.setProperties(properties);


        //La idea va a ser, crear una y guardarme el id.
        Sheets.Spreadsheets.Create request = mService.spreadsheets().create(requestBody);
        //Sheets.Spreadsheets.Get request = mService.spreadsheets().get("1BjMo4XuTtS64msluVVzepPVQkNxTeACbfdffc6dmJIs");
        //String range = "Class Data!A2:E";
        //String range = "<HOJA>!<COL><FILA>:<COL><FILA>";

        return request.execute();
    }
}