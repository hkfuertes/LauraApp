package net.mfuertes.laurapp.lauraapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import net.mfuertes.laurapp.lauraapp.Fragments.guardia
import net.mfuertes.laurapp.lauraapp.Fragments.planta
import net.mfuertes.laurapp.lauraapp.Fragments.ptes_ic
import net.mfuertes.laurapp.lauraapp.Fragments.sesion
import java.util.zip.ZipEntry

class FragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        switchFragments(receiveFragment())
    }

    private fun receiveFragment(): Fragment?{
        val id = this.intent.getSerializableExtra("fragment_id") as String
        when(id){
            Integer(R.drawable.planta).toString()->
                    return planta()
            Integer(R.drawable.ptes_ic).toString() ->
                    return ptes_ic()
            Integer(R.drawable.sesion).toString() ->
                    return sesion()
            Integer(R.drawable.guardias).toString() ->
                    return guardia()
        }
        return null
    }


    fun switchFragments(fragment: Fragment?) {
        if(fragment !== null) {
            val manager = supportFragmentManager
            manager.beginTransaction().replace(R.id.fragment_placeholder_single, fragment).commit()
        }
    }

}
