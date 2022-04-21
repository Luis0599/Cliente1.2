package com.example.cliente12.`interface`

import retrofit.client.Response
import retrofit.http.Field
import retrofit.http.FormUrlEncoded
import retrofit.http.POST
import retrofit.Callback
interface Login {
    @FormUrlEncoded
    @POST("/InicioSesion.php")
    fun VerficarUsuario(
        @Field("telefonoCliente") telefonoCliente: String,
        @Field("Contrasena") Contrasena: String,
        callback: Callback<Response?>
    )
}