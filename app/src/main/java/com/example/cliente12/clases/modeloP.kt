package com.example.cliente12.clases

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class modeloP {
    @SerializedName("idPresupuestoNoRegistrado")
    @Expose
    private var idPresupuestoNoRegistrado: Int = 0

    @SerializedName("fechaPresupuesto")
    @Expose
    private var fechaPresupuesto: String = ""

    @SerializedName("Nombre")
    @Expose
    private var Nombre: String = ""

    @SerializedName("Telefono")
    @Expose
    private var Telefono: String = ""

    @SerializedName("Calle")
    @Expose
    private var Calle: String = ""

    @SerializedName("Colonia")
    @Expose
    private var Colonia: String = ""

    @SerializedName("No_Exterior")
    @Expose
    private var No_Exterior: String = ""

    @SerializedName("problema")
    @Expose
    private var problema: String = ""

    @SerializedName("PagoTotal")
    @Expose
    private var PagoTotal: Double = 0.0


    fun getIdPresupuestoNoRegistrado(): Int { return idPresupuestoNoRegistrado}

    fun getFechaPresupuesto(): String { return fechaPresupuesto}

    fun getNombre(): String { return Nombre }

    fun getTelefono(): String { return Telefono }

    fun getCalle(): String { return Calle }

    fun getColonia(): String { return Colonia }

    fun getNumeroExterior(): String { return No_Exterior }

    fun getPagoTotal(): Double { return PagoTotal }

    fun getProblema(): String { return problema }



    fun setIdPresupuestoNoRegistrado(i: Int) { idPresupuestoNoRegistrado = i }

    fun setFechaPresupuesto(f: String) { fechaPresupuesto = f}

    fun setNombre(n: String) { Nombre = n }

    fun setTelefono(t: String) { Telefono = t }

    fun setCalle(calle: String) { Calle = calle }

    fun setColonia(colonia: String) { Colonia = colonia }

    fun setNumeroExterior(num: String) { No_Exterior = num }

    fun setProblema(p: String) { problema = p }

    fun setPagoTotal(pago: Double) { PagoTotal = pago }
}