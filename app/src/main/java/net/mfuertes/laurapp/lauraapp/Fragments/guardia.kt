package net.mfuertes.laurapp.lauraapp.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.util.Log
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

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [guardia.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [guardia.newInstance] factory method to
 * create an instance of this fragment.
 */
class guardia : Fragment(), SheetTask.OnFinishListener {
    override fun onFinish(sheet: Spreadsheet?) {
        activity.finish()
    }

    private var mCredential: GoogleAccountCredential? = null
    private var mSheetId: String? = null
    val SCOPES = arrayOf(SheetsScopes.DRIVE, SheetsScopes.SPREADSHEETS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity.title = getString(R.string.title_guardias)
        setHasOptionsMenu(true);

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(activity.applicationContext, Arrays.asList(*SCOPES)).setBackOff(ExponentialBackOff())
        val accountName = PreferenceManager.getDefaultSharedPreferences(activity).getString(getString(R.string.PREF_ACCOUNT_NAME), null)
        if (accountName != null) {
            mCredential!!.selectedAccountName = accountName
        }

        mSheetId = PreferenceManager.getDefaultSharedPreferences(activity).getString(getString(R.string.sheet_id_key),null)
    }

    private lateinit var fecha: DatePicker
    private lateinit var tipo: TextView
    private lateinit var numero: TextView
    private lateinit var motivo: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_guardia, container, false)
        fecha = view.findViewById(R.id.guardia_fecha) as DatePicker
        tipo = view.findViewById(R.id.guardia_tipo) as TextView
        numero = view.findViewById(R.id.guardia_numero) as TextView
        motivo = view.findViewById(R.id.guardia_motivo) as TextView

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
                values.add(String.format("%02d", fecha.dayOfMonth) + "/" + String.format("%02d", fecha.month) + "/" + fecha.year.toString())
                values.add(tipo.text.toString())
                values.add(numero.text.toString())
                values.add(motivo.text.toString())

                AppendSheetTask(mCredential!!,getString(R.string.app_name),this,mSheetId!!,getString(R.string.title_guardias),values).execute()

                return true
            }
            else -> {
            }
        }

        return false
    }
}
