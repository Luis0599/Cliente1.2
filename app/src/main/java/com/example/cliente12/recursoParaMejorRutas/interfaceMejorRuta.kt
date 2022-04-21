package com.example.cliente12.recursoParaMejorRutas

import retrofit.http.FormUrlEncoded
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface interfaceMejorRuta {
    @FormUrlEncoded
    @GET("obtenerCoordenadasParaSaberElKerklyMasCercano.php")
    open fun ObtenerC(@Query("oficio") oficio: String):
            Call<List<ModeloRutas?>?>?
}