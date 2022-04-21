package com.example.cliente12.`interface`

import retrofit.Callback
import retrofit.client.Response
import retrofit.http.Field
import retrofit.http.FormUrlEncoded
import retrofit.http.POST
interface RecuperarCuenta {
    @FormUrlEncoded
    @POST("/RecuperarCuenta.php")
    fun VerficarUsuario(
        @Field("Correo") Correo: String,
        callback: Callback<Response?>
    )
}