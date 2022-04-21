package com.example.cliente12.`interface`

import retrofit.Callback
import retrofit.client.Response
import retrofit.http.Field
import retrofit.http.FormUrlEncoded
import retrofit.http.POST

interface EntrarSinRegistro {
    @FormUrlEncoded
    @POST("/pruebaSinRegistroNumero.php")
    fun sinRegistro(
        @Field("telefonoCliente") telefonoCliente: String,
        callback: Callback<Response?>
    )
}