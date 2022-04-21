package com.example.cliente12.`interface`

import retrofit.Callback
import retrofit.client.Response
import retrofit.http.Field
import retrofit.http.FormUrlEncoded
import retrofit.http.POST

interface Registro_Login {
    @FormUrlEncoded
    @POST("/Login.php")
    fun insertUser(
        @Field("Correo") Correo: String?,
        @Field("Nombre") Nombre: String?,
        @Field("Apellido_Paterno") Apellido_Paterno: String?,
        @Field("Apellido_Materno") Apellido_Materno: String?,
        // @Field("Dirección_idDirección") Dirección_idDirección: String?,
        @Field("telefonoCliente") telefonoCliente: String?,
        @Field("generoCliente") generoCliente: String?,
        @Field("Contrasena") Contrasena: String?,
        @Field("fue_NoRegistrado") fue_NoRegistrado: String?,
        callback: Callback<Response?>
    )
}