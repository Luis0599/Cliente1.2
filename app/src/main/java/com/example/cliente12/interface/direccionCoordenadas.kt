package com.example.cliente12.`interface`


import retrofit.Callback
import retrofit.client.Response
import retrofit.http.Field
import retrofit.http.FormUrlEncoded
import retrofit.http.POST
interface direccionCoordenadas {
    @FormUrlEncoded
    @POST("/EnviarPruebaSinRegistro.php")
    fun sinRegistro(
        @Field("id_oficioK") TipoServicio: Int,
        @Field("problema") Problematica: String,
        @Field("latitud") latitud: String,
        @Field("longitud") longitud: String,
        @Field("Calle") Calle: String,
        @Field("Colonia") Colonia: String,
        @Field("No_Interior") No_Interior: String,
        @Field("No_Exterior") No_Exterior: String,
        @Field("Ciudad") Ciudad: String,
        @Field("Estado") Estado: String,
        @Field("Pais") Pais: String,
        @Field("Codigo_Postal") Codigo_Postal: String,
        @Field("Referencia") Referencia: String,
        @Field("numeroRP") numeroRP: String,

        @Field("nombre_noR") nombre_noR: String,
        @Field("apellidoP_noR") apellidoP_noR: String,
        @Field("apellidoM_noR") apellidoM_noR: String,
        callback: Callback<Response?>
    )
}