package net.mfuertes.laurapp.lauraapp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_list.*
import net.mfuertes.laurapp.lauraapp.Adapters.MainMenuAdapter
import net.mfuertes.laurapp.lauraapp.Fragments.guardia
import net.mfuertes.laurapp.lauraapp.Fragments.planta
import net.mfuertes.laurapp.lauraapp.Fragments.ptes_ic
import net.mfuertes.laurapp.lauraapp.Fragments.sesion

class ListActivity : AppCompatActivity(), MainMenuAdapter.OnItemClickListener {

    private var ptes_ic_fragment : ptes_ic = ptes_ic()
    private var planta_fragment : planta = planta()
    private var sesion_fragment: sesion = sesion()
    private var guardia_fragment: guardia = guardia()

    override fun onItemClick(id: String) {
        val intent = Intent(this,FragmentActivity::class.java)
        intent.putExtra("fragment_id", id)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar)

        val recyclerView: RecyclerView = findViewById(R.id.main_menu)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MainMenuAdapter(generateMenu(), this)

        setTitle("Funcacion App")

        /*
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        */
    }

    private fun generateMenu(): List<String>{
        val items = mutableListOf<String>()
        items.add(Integer(R.drawable.planta).toString() + ",Planta,Pacientes de planta...")
        items.add(Integer(R.drawable.ptes_ic).toString() + ",Pacientes IC,Pacientes de Interconsulta...")
        items.add(Integer(R.drawable.sesion).toString() + ",Sesiones,Sesiones...")
        items.add(Integer(R.drawable.guardias).toString() + ",Guardias,Pacientes de la guardia...")
        return items
    }
}
