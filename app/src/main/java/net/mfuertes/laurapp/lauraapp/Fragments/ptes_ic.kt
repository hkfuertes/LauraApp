package net.mfuertes.laurapp.lauraapp.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.util.Log

import net.mfuertes.laurapp.lauraapp.R
import android.view.*
import android.widget.TextView
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.Spreadsheet
import net.mfuertes.laurapp.lauraapp.Connection.AppendSheetTask
import net.mfuertes.laurapp.lauraapp.Connection.SheetTask
import net.mfuertes.laurapp.lauraapp.SingleSettingsActivity
import java.util.*


class ptes_ic : Fragment(), SheetTask.OnFinishListener {
    override fun onFinish(sheet: Spreadsheet?) {
        activity.finish()
    }


    private var mCredential: GoogleAccountCredential? = null
    val SCOPES = arrayOf(SheetsScopes.DRIVE, SheetsScopes.SPREADSHEETS)


    private var mSheetId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity.title = getString(R.string.title_ptes_ic)
        setHasOptionsMenu(true);

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(activity.applicationContext, Arrays.asList(*SCOPES)).setBackOff(ExponentialBackOff())
        val accountName = PreferenceManager.getDefaultSharedPreferences(activity).getString(getString(R.string.PREF_ACCOUNT_NAME), null)
        Log.d("TEST_PTES",accountName)
        if (accountName != null) {
            mCredential!!.selectedAccountName = accountName
        }

        mSheetId = PreferenceManager.getDefaultSharedPreferences(activity).getString(getString(R.string.sheet_id_key),null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fragment_accept ->{

                val values: ArrayList<String> = ArrayList<String>()
                values.add(num.text.toString())
                values.add(age.text.toString())
                values.add(motive.text.toString())
                values.add(service.text.toString())
                values.add(solution.text.toString())

                AppendSheetTask(mCredential!!,getString(R.string.app_name),this,mSheetId!!,getString(R.string.title_ptes_ic),values).execute()

                return true
            }
            else -> {
            }
        }

        return false
    }

    private lateinit var num: TextView
    private lateinit var age: TextView
    private lateinit var motive: TextView
    private lateinit var service: TextView
    private lateinit var solution: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_ptes_ic, container, false)

        num = view.findViewById(R.id.ptes_ic_numero) as TextView
        age = view.findViewById(R.id.ptes_ic_edad) as TextView
        motive = view.findViewById(R.id.ptes_ic_motivo_ppal) as TextView
        service = view.findViewById(R.id.ptes_ic_serv_sol) as TextView
        solution = view.findViewById(R.id.ptes_ic_resolucion) as TextView


        return view
    }

}
