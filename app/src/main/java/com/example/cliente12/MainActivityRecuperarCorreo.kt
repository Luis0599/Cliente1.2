package com.example.cliente12

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cliente12.`interface`.RecuperarCuenta
import com.example.cliente12.clases.ClaseUrl
import retrofit.Callback
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.Response
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivityRecuperarCorreo : AppCompatActivity() {
    lateinit var correo: EditText;
    lateinit var txtM: TextView

    lateinit var btnCorreobuscar: Button
    var url = ClaseUrl()
    val ROOT_URL = url.ROOT_URL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_recuperar_correo)

        correo = findViewById(R.id.txtCorreoR)
        txtM = findViewById(R.id.txtM)
        btnCorreobuscar = findViewById(R.id.btnbuscarCorreo)
        btnCorreobuscar.setOnClickListener(View.OnClickListener {
            RecuperarCuenta()

        })
    }

    private fun RecuperarCuenta() {

        val adapter = RestAdapter.Builder()
            .setEndpoint(ROOT_URL)
            .build()
        val api: RecuperarCuenta = adapter.create(RecuperarCuenta::class.java)
        api.VerficarUsuario(correo.text.toString(),
            object : Callback<Response?> {
                override fun success(t: Response?, response: Response?) {
                    var entrada: BufferedReader? = null
                    var Respuesta = ""
                    try {
                        entrada = BufferedReader(InputStreamReader(t?.body?.`in`()))
                        Respuesta = entrada.readLine()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    Toast.makeText(this@MainActivityRecuperarCorreo, Respuesta, Toast.LENGTH_SHORT)
                        .show()
                    var Res = "correcto";
                    txtM.setText(Respuesta)
                    if (Res.equals(Respuesta)) {
                        Toast.makeText(
                            this@MainActivityRecuperarCorreo,
                            "Todo exelente, ya pasaria a la sig actividad",
                            Toast.LENGTH_SHORT
                        ).show()
                        //enviamos el correo a la otra actividad para poder modificar la contraseña

                        var intent = Intent(this@MainActivityRecuperarCorreo, MainActivityModificarContrasena::class.java)
                        intent.putExtra("correo", correo.text.toString())
                        startActivity(intent)
                        finish()
                    }
                }

                override fun failure(error: RetrofitError?) {
                    Toast.makeText(
                        this@MainActivityRecuperarCorreo,
                        "error " + error,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        )
    }
}