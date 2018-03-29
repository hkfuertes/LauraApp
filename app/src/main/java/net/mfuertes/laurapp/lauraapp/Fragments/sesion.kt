package net.mfuertes.laurapp.lauraapp.Fragments


import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.*
import android.widget.DatePicker
import android.widget.TextView
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.Spreadsheet
import net.mfuertes.laurapp.lauraapp.Connection.AppendSheetTask
import net.mfuertes.laurapp.lauraapp.Connection.SheetTask

import net.mfuertes.laurapp.lauraapp.R
import java.util.*


class sesion : Fragment(), SheetTask.OnFinishListener {
    override fun onFinish(sheet: Spreadsheet?) {
        activity.finish()
    }

    private var mCredential: GoogleAccountCredential? = null
    private var mSheetId: String? = null
    val SCOPES = arrayOf(SheetsScopes.DRIVE, SheetsScopes.SPREADSHEETS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity.title = getString(R.string.title_sesion)
        setHasOptionsMenu(true);

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(activity.applicationContext, Arrays.asList(*SCOPES)).setBackOff(ExponentialBackOff())
        val accountName = PreferenceManager.getDefaultSharedPreferences(activity).getString(getString(R.string.PREF_ACCOUNT_NAME), null)
        if (accountName != null) {
            mCredential!!.selectedAccountName = accountName
        }

        mSheetId = PreferenceManager.getDefaultSharedPreferences(activity).getString(getString(R.string.sheet_id_key),null)
    }


    private lateinit var tipo: TextView
    private lateinit var sitio: TextView
    private lateinit var titulo: TextView
    private lateinit var fecha: DatePicker
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_sesion, container, false)

        tipo = view.findViewById(R.id.sesion_tipo) as TextView
        sitio = view.findViewById(R.id.sesion_sitio) as TextView
        titulo = view.findViewById(R.id.sesion_titulo) as TextView
        fecha = view.findViewById(R.id.sesion_fecha) as DatePicker


        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fragment_accept ->{

                val values: ArrayList<String> = ArrayList<String>()
                values.add(tipo.text.toString())
                values.add(sitio.text.toString())
                values.add(titulo.text.toString())
                values.add(String.format("%02d", fecha.dayOfMonth) + "/" + String.format("%02d", fecha.month) + "/" + fecha.year.toString())


                AppendSheetTask(mCredential!!,getString(R.string.app_name),this,mSheetId!!,getString(R.string.title_guardias),values).execute()

                return true
            }
            else -> {
            }
        }

        return false
    }
}
