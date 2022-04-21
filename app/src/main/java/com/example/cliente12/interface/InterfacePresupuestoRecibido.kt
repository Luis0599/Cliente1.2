package com.example.cliente12.`interface`

import com.example.cliente12.clases.modeloP
import retrofit.http.FormUrlEncoded
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.*
interface InterfacePresupuestoRecibido {
    @FormUrlEncoded
    @GET("PresupuestoRecibido.php")
    open fun EnviarT(@Query("telefono_NoR") telefono_NoR: String):
            Call<List<modeloP?>?>?
}