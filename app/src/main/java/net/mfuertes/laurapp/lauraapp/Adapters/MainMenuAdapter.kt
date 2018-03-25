package net.mfuertes.laurapp.lauraapp.Adapters

import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_main_menu.view.*
import net.mfuertes.laurapp.lauraapp.R

/**
 * Created by hkfuertes on 25-Mar-18.
 */
class MainMenuAdapter (private val values: List<String>, val listener: OnItemClickListener): RecyclerView.Adapter<MainMenuAdapter.ViewHolder>(), View.OnClickListener {
    override fun onClick(view: View?) {
        listener.onItemClick((view?.getTag()) as String)
    }

    override fun getItemCount()= values.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.itemView?.setOnClickListener(this)
        val parts = values.get(position).split(',')
        holder?.titulo?.text = parts[1]
        holder?.subtitulo?.text = parts[2]
        holder?.icono?.setImageResource(Integer.parseInt(parts[0]))
        holder?.itemView?.setTag(parts[0])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_main_menu, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        var titulo: TextView? = null
        var subtitulo: TextView? = null
        var icono: ImageView? = null
        init{
            titulo = itemView?.findViewById(R.id.main_menu_text)
            subtitulo = itemView?.findViewById(R.id.main_menu_subtext)
            icono = itemView?.findViewById(R.id.main_menu_icon)
        }
    }


    interface OnItemClickListener{
        fun onItemClick(id: String)
    }
}