package net.mfuertes.laurapp.lauraapp.Fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import net.mfuertes.laurapp.lauraapp.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [planta.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [planta.newInstance] factory method to
 * create an instance of this fragment.
 */
class planta : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
        activity.title = getString(R.string.title_planta)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_planta, container, false) as View

        var dialog_button = view.findViewById(R.id.planta_adjuntar) as Button

        dialog_button.setOnClickListener(View.OnClickListener { _ ->
            showNewNameDialog()
        })

        return view
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

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment planta.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): planta {
            val fragment = planta()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
