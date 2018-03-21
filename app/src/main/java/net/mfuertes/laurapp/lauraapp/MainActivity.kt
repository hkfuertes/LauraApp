package net.mfuertes.laurapp.lauraapp

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import net.mfuertes.laurapp.lauraapp.Fragments.guardia
import net.mfuertes.laurapp.lauraapp.Fragments.planta
import net.mfuertes.laurapp.lauraapp.Fragments.ptes_ic
import net.mfuertes.laurapp.lauraapp.Fragments.sesion



class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when(item.itemId){
            R.id.navigation_planta -> {
                switchFragments(planta_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_ptes_ic -> {
                switchFragments(ptes_ic_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_sesion -> {
                switchFragments(sesion_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_guardias -> {
                switchFragments(guardia_fragment)
                return@OnNavigationItemSelectedListener true
            }

        }
         false
    }

    private var ptes_ic_fragment : ptes_ic = ptes_ic()
    private var planta_fragment : planta = planta()
    private var sesion_fragment: sesion = sesion()
    private var guardia_fragment: guardia = guardia()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_planta
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    fun switchFragments(fragment: Fragment) {
        val manager = supportFragmentManager
        manager.beginTransaction().replace(R.id.fragment_placeholder, fragment).commit()
    }

}