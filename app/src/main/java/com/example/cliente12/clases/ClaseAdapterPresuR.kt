package com.example.cliente12.clases

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cliente12.R

class ClaseAdapterPresuR (val datset: ArrayList<modeloP>) :
    RecyclerView.Adapter<ClaseAdapterPresuR.ViewHolder>(), View.OnClickListener {
    private lateinit var listener: View.OnClickListener
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idPresupuesto: TextView
        init {

            idPresupuesto =  view.findViewById(R.id.idPresupuesto)
        }
        val fechaPresupuesto: TextView
        init {
            fechaPresupuesto = view.findViewById(R.id.fechaPresupuesto)
        }
        val nombreR: TextView
        init {

            nombreR  = view.findViewById(R.id.nombreR)
        }
        val telefonoR: TextView
        init {

            telefonoR = view.findViewById(R.id.telefonoR)
        }

        val calleR: TextView
        init {

            calleR = view.findViewById(R.id.calleR)
        }

        val coloniaR: TextView
        init {

            coloniaR = view.findViewById(R.id.coloniaR)
        }
        val noExteriorR: TextView
        init {

            noExteriorR = view.findViewById(R.id.noExteriorR)
        }
        val problemaR: TextView
        init {

            problemaR = view.findViewById(R.id.problemaR)
        }
        val pagoTotalR: TextView
        init {

            pagoTotalR = view.findViewById(R.id.pagoTotalR)
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ClaseAdapterPresuR.ViewHolder, position: Int) {
        viewHolder.idPresupuesto.text = datset.get(position).getIdPresupuestoNoRegistrado().toString()
        viewHolder.fechaPresupuesto.text = datset.get(position).getFechaPresupuesto().toString()
        viewHolder.nombreR.text = datset.get(position).getNombre().toString()
        viewHolder.telefonoR.text = datset.get(position).getTelefono().toString()
        viewHolder.calleR.text = datset.get(position).getCalle().toString()
        viewHolder.coloniaR.text = datset.get(position).getColonia().toString()
        viewHolder.noExteriorR.text = datset.get(position).getNumeroExterior().toString()
        viewHolder.problemaR.text = datset.get(position).getProblema().toString()
        viewHolder.pagoTotalR.text = datset.get(position).getPagoTotal().toString()

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.modelo_presupuesto, viewGroup, false)
        view.setOnClickListener(this)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datset.size
    }

    fun setOnClickListener(l: View.OnClickListener) {
        this.listener = l
    }

    override fun onClick(v: View?) {
        listener.onClick(v)
    }
}