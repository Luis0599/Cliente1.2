package com.example.cliente12.recursoParaMejorRutas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModeloRutas {
    @SerializedName("Curp")
    @Expose
    private var Curp: String = ""
    @SerializedName("latitud")
    @Expose
    private var latitud =0.0
    @SerializedName("longitud")
    @Expose
    private var longitud =0.0

    fun getCurp(): String { return Curp}
    fun getLatitud(): Double { return latitud}
    fun getLongitud(): Double { return longitud}
}