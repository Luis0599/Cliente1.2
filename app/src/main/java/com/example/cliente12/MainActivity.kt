package com.example.cliente12

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.cliente12.`interface`.EntrarSinRegistro
import com.example.cliente12.`interface`.Registro_Login
import com.example.cliente12.clases.ClaseUrl
import kotlinx.android.synthetic.main.activity_main.*
import retrofit.Callback
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    lateinit var correo: EditText
    lateinit var nombre: EditText
    lateinit var apeP: EditText
    lateinit var apeM: EditText
    lateinit var telefono: EditText
    lateinit var genero: EditText
    lateinit var contra: EditText
    lateinit var NoRegistrado: EditText

    lateinit var btnEnviar: Button
    lateinit var json: TextView
    lateinit var textcerrar: TextView
    lateinit var  btnInicioS: Button
    lateinit var  btnRecup: Button
    lateinit var btnSinRegistro: Button
    lateinit var btnmostrarPresupuesto: Button

    var myVentana: Dialog? =null;

    lateinit var editexNombre: EditText
    lateinit var editexApeP: EditText
    lateinit var editexApeM: EditText
    lateinit var txtNumeroT: EditText

    var url = ClaseUrl()

    val ROOT_URL = url.ROOT_URL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       verificarConecion()

        btnTrazarLinea.setOnClickListener {
            var intent2 =Intent(this@MainActivity, MapsActivityTrazarMejorRuta::class.java)
                    startActivity(intent2)
        }


    }

    @SuppressLint("MissingPermission")
    fun verificarConecion (){
        val manager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        if (networkInfo != null) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                // CONNECTED
                // Toast.makeText(this@MainActivity, "si tienes conexion", Toast.LENGTH_SHORT).show()
                correo = findViewById(R.id.txtCorreo)
                nombre = findViewById(R.id.txtNombre)
                apeP = findViewById(R.id.txtApellidoP)
                apeM = findViewById(R.id.txtApellidoM)
                telefono = findViewById(R.id.txtTelefono)
                genero = findViewById(R.id.txtGenero)
                contra = findViewById(R.id.txtContraseña)
                NoRegistrado = findViewById(R.id.txtFueRegistrado)
                btnEnviar = findViewById(R.id.btnEnviar)
                btnEnviar.setOnClickListener(View.OnClickListener {
                    InsertarMysql()


                })
                json = findViewById(R.id.txtJson)
                btnInicioS = findViewById(R.id.btniniciosesion)
                btnInicioS.setOnClickListener {
                    val intent = Intent(this, MainActivityIniciarSesion::class.java)
                    startActivity(intent)
                }
                btnRecup = findViewById(R.id.btnRecuperarC)
                btnRecup.setOnClickListener {
                    val intent = Intent(this, MainActivityRecuperarCorreo::class.java)
                    startActivity(intent)
                }
                myVentana = Dialog(this)

                btnSinRegistro = findViewById(R.id.buttonSinRegistro)
                btnSinRegistro.setOnClickListener {

                    myVentana?.setContentView(R.layout.ventanita)
                    myVentana!!.show()
                    textcerrar = myVentana?.findViewById(R.id.cerrar)!!
                    textcerrar.setOnClickListener{
                        myVentana!!.dismiss()
                    }
                    //  entrar()

                }
                btnmostrarPresupuesto = findViewById(R.id.btnmostrarPresupuesto)
                btnmostrarPresupuesto.setOnClickListener {
                    presupuestoR()
                }
            }
        }else {
            // DISCONNECTED"
            Toast.makeText(this@MainActivity, "No tienes conexion", Toast.LENGTH_SHORT).show()

        }


    }
    fun sinRegistros(){
        editexNombre = myVentana?.findViewById(R.id.editexNombreV)!!
        editexApeP = myVentana?.findViewById(R.id.editexApP_V)!!
        editexApeM = myVentana?.findViewById(R.id.editexApeM_V)!!
        txtNumeroT = myVentana?.findViewById(R.id.editTextPhoneSinR)!!
        PruebaRegistrarNumero()
    }

    private fun PruebaRegistrarNumero() {
        val adapter = RestAdapter.Builder()
            .setEndpoint(ROOT_URL)
            .build()
        val api: EntrarSinRegistro = adapter.create(EntrarSinRegistro::class.java)
        api.sinRegistro(txtNumeroT.text.toString(),

            object : Callback<Response?> {
                override fun success(t: Response?, response: Response?) {
                    var salida: BufferedReader? = null
                    var entrada = ""
                    try {
                        salida = BufferedReader(InputStreamReader(t?.body?.`in`()))
                        entrada = salida.readLine()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    Toast.makeText(this@MainActivity, entrada, Toast.LENGTH_SHORT).show()
                    if (entrada.equals(txtNumeroT.text.toString())) {
                        entrar()
                    } else {
                        Toast.makeText(this@MainActivity, entrada, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun failure(error: RetrofitError?) {
                    println("error$error")

                }

            })
    }
    fun entrar(){
        val intent = Intent(this, MainActivityConsultaSinRegistro::class.java)
        intent.putExtra("numSinRegistro", txtNumeroT.text.toString())
        intent.putExtra("nombreN", editexNombre.text.toString())
        intent.putExtra("apellidoP", editexApeP.text.toString())
        intent.putExtra("apellidoM", editexApeM.text.toString())
        startActivity(intent)
    }

    fun presupuestoR(){
      val intent = Intent(this, MainActivityRecibirPresupuesto::class.java)
        intent.putExtra("numero", "7471503418")
        startActivity(intent)
       /*val intent = Intent(this, MainActivityColeccionFirebase::class.java)
        startActivity(intent);*/
    }

    fun InsertarMysql() {
        val adapter = RestAdapter.Builder()
            .setEndpoint(ROOT_URL)
            .build()
        val api: Registro_Login = adapter.create(Registro_Login::class.java)
        api.insertUser(
            correo.text.toString(),
            nombre.text.toString(),
            apeP.text.toString(),
            apeM.text.toString(),
            //"0",
            telefono.text.toString(),
            genero.text.toString(),
            contra.text.toString(),
            NoRegistrado.text.toString(),
            object : Callback<Response?> {
                override fun success(t: Response?, response: Response?) {
                    var reader: BufferedReader? = null
                    var output = ""
                    try {
                        reader = BufferedReader(InputStreamReader(t?.getBody()?.`in`()))
                        output = reader.readLine()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    //Toast.makeText(this, output, Toast.LENGTH_LONG).show();
                    Toast.makeText(this@MainActivity, output, Toast.LENGTH_SHORT).show()
                    //println("aqui $output")
                    json.setText(output)
                }

                override fun failure(error: RetrofitError?) {
                    Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_SHORT).show()
                }

            }
        );
    }
}