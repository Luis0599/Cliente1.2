package com.example.cliente12

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.cliente12.`interface`.Login
import com.example.cliente12.clases.ClaseUrl
import retrofit.Callback
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.util.*

class MainActivityIniciarSesion : AppCompatActivity() {
    lateinit var usuario: EditText;
    lateinit var contraseña: EditText
    lateinit var btnIniciar: Button
    lateinit var txtHash: TextView

    var url = ClaseUrl()
    val ROOT_URL = url.ROOT_URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_iniciar_sesion)

        usuario = findViewById(R.id.editTextPhone);
        contraseña = findViewById(R.id.editTextTextPassword)
        txtHash = findViewById(R.id.txtHash)

        btnIniciar = findViewById(R.id.btnIniciar)
        btnIniciar.setOnClickListener(View.OnClickListener {
            VerficiarUsuario();
        })
    }

    private fun VerficiarUsuario() {

        val adapter = RestAdapter.Builder()
            .setEndpoint(ROOT_URL)
            .build()
        val api: Login = adapter.create(Login::class.java)
        api.VerficarUsuario(usuario.text.toString(),
            contraseña.text.toString(),
            object : Callback<Response?>{
                override fun success(t: Response?, response: Response?) {
                    var entrada: BufferedReader? =  null
                    var Respuesta = ""
                    try {
                        entrada = BufferedReader(InputStreamReader(t?.body?.`in`()))
                        Respuesta = entrada.readLine()
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                    Toast.makeText(this@MainActivityIniciarSesion, Respuesta, Toast.LENGTH_SHORT).show()
                    txtHash.setText(Respuesta)
                    var Res = "Bienvenido";
                    if (Res.equals(Respuesta)){
                        Toast.makeText(this@MainActivityIniciarSesion, "Todo exelente, ya pasaria a la sig actividad", Toast.LENGTH_SHORT).show()
                        var  intent = Intent(this@MainActivityIniciarSesion, MainActivityConsultaSinRegistro::class.java)
                        startActivity(intent)

                    }
                }

                override fun failure(error: RetrofitError?) {
                    Toast.makeText(this@MainActivityIniciarSesion, "error " + error, Toast.LENGTH_SHORT).show()
                }

            }
        )
    }
}