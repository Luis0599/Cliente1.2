package com.example.cliente12.`interface`


import retrofit.http.Field
import retrofit.http.FormUrlEncoded
import retrofit.http.POST
import retrofit.Callback
import retrofit.client.Response

interface AceptarPresupuesto {
    @FormUrlEncoded
    @POST("/AceptarPresupuesto.php")
    fun Aceptar(
        @Field("idPresupuestoNoRegistrado")  idPresupuestoNoRegistrado: String,
        @Field("aceptoCliente") aceptoCliente: String,
        callback: Callback<Response?>
    )
}