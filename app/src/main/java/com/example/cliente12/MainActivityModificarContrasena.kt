package com.example.cliente12

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.cliente12.`interface`.ModificarContraseña
import com.example.cliente12.clases.ClaseUrl
import retrofit.Callback
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.Response
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivityModificarContrasena : AppCompatActivity() {
    lateinit var txtNuevaC: EditText
    lateinit var txtConfirmarNuevaC: EditText
    lateinit var btnModificar: Button
    lateinit var txtR: TextView

    var url = ClaseUrl()
    val ROOT_URL = url.ROOT_URL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_modificar_contrasena)


        txtNuevaC = findViewById(R.id.txtContraseña)
        txtConfirmarNuevaC = findViewById(R.id.txtConfirmarContraseña)
        txtR = findViewById(R.id.tewViewR)
        btnModificar = findViewById(R.id.btnCambiarContra)
        btnModificar.setOnClickListener {
            modificarContra()

        }

    }

    /*private  fun check(){
        //obtenemos el correo de la actividad MainActivityRecuperarCorreo
        val intent = intent
        val correoRecibido = intent.getStringExtra("correo")
        Toast.makeText(this, correoRecibido, Toast.LENGTH_SHORT).show()
    }*/
    private fun modificarContra() {
        //primero validamos que la contraseña sean igual
        if (txtNuevaC.text.toString().equals(txtConfirmarNuevaC.text.toString())) {
            //obtenemos el correo de la actividad MainActivityRecuperarCorreo
            val intent = getIntent()
            val correoRecibido = intent.getStringExtra("correo")

            val adapter = RestAdapter.Builder().setEndpoint(ROOT_URL)
                .build()
            val api: ModificarContraseña = adapter.create(ModificarContraseña::class.java)
            api.VerficarUsuario(correoRecibido.toString(),
                txtConfirmarNuevaC.text.toString(),

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

                        Toast.makeText(this@MainActivityModificarContrasena, Respuesta, Toast.LENGTH_SHORT).show()
                        txtR.setText(Respuesta)
                        /* var Res = "correcto";

                        if (Res.equals(Respuesta)) {
                            Toast.makeText(this@MainActivityModificarContrasena, "Todo exelente, ya pasaria a la sig actividad", Toast.LENGTH_SHORT).show()
                            var intent = Intent(this@MainActivityModificarContrasena, MainActivity::class.java
                            )
                            startActivity(intent)
                            finish()
                        }*/
                    }

                    override fun failure(error: RetrofitError?) {
                        Toast.makeText(
                            this@MainActivityModificarContrasena,
                            "error " + error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            )
        } else{
            Toast.makeText(this, "La contraseña no son iguales", Toast.LENGTH_SHORT).show();
        }

    }
}