package com.example.cliente12.`interface`
import retrofit.Callback
import retrofit.client.Response
import retrofit.http.Field
import retrofit.http.FormUrlEncoded
import retrofit.http.POST

interface ModificarContraseña {
    @FormUrlEncoded
    @POST("/RecuperarCuentaContra.php")
    fun VerficarUsuario(
        @Field("Correo") Correo: String,
        @Field("Contrasena") Contrasena: String,
        callback: Callback<Response?>
    )
}