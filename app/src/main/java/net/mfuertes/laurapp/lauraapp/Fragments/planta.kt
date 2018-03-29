package net.mfuertes.laurapp.lauraapp.Fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.*
import android.widget.TextView
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.Spreadsheet
import net.mfuertes.laurapp.lauraapp.Connection.AppendSheetTask
import net.mfuertes.laurapp.lauraapp.Connection.SheetTask
import net.mfuertes.laurapp.lauraapp.R
import java.util.*


class planta : Fragment(), SheetTask.OnFinishListener {
    override fun onFinish(sheet: Spreadsheet?) {
        activity.finish()
    }

    private var mCredential: GoogleAccountCredential? = null
    private var mSheetId: String? = null
    val SCOPES = arrayOf(SheetsScopes.DRIVE, SheetsScopes.SPREADSHEETS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity.title = getString(R.string.title_planta)

        setHasOptionsMenu(true);

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(activity.applicationContext, Arrays.asList(*SCOPES)).setBackOff(ExponentialBackOff())
        val accountName = PreferenceManager.getDefaultSharedPreferences(activity).getString(getString(R.string.PREF_ACCOUNT_NAME), null)
        if (accountName != null) {
            mCredential!!.selectedAccountName = accountName
        }

        mSheetId = PreferenceManager.getDefaultSharedPreferences(activity).getString(getString(R.string.sheet_id_key),null)
    }

    private lateinit var numero: TextView
    private lateinit var edad: TextView
    private lateinit var motivop: TextView
    private lateinit var motivos: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_planta, container, false) as View

        /*
        var dialog_button = view.findViewById(R.id.planta_adjuntar) as Button
        dialog_button.setOnClickListener(View.OnClickListener { _ ->
            showNewNameDialog()
        })
        */

        numero = view.findViewById(R.id.planta_numero) as TextView
        edad = view.findViewById(R.id.planta_edad) as TextView
        motivop = view.findViewById(R.id.planta_motivo_ppal) as TextView
        motivos = view.findViewById(R.id.planta_motivo_sec) as TextView

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
                values.add(numero.text.toString())
                values.add(edad.text.toString())
                values.add(motivop.text.toString())
                values.add(motivos.text.toString())

                AppendSheetTask(mCredential!!,getString(R.string.app_name),this,mSheetId!!,getString(R.string.title_planta),values).execute()

                return true
            }
            else -> {
            }
        }

        return false
    }


    fun showNewNameDialog() {
        val dialogBuilder = AlertDialog.Builder(this.activity)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.planta_adjuntar_archivo, null)
        dialogBuilder.setView(dialogView)

        //val editText = dialogView.findViewById<View>(R.id.editTextName) as EditText

        dialogBuilder.setTitle("Adjuntar Archivo")
        //dialogBuilder.setMessage("Enter Name Below")
        dialogBuilder.setPositiveButton(R.string.guardar, DialogInterface.OnClickListener { dialog, whichButton ->
            //do something with edt.getText().toString();

            // Add Name in list
            //nameList.add(editText.text.toString())
            // Handler code here.
            //val intent = Intent(this, NewKitListActivity::class.java)
            //startActivity(intent);

        })
        dialogBuilder.setNegativeButton(R.string.cancelar, DialogInterface.OnClickListener { dialog, whichButton ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
    }
}
