package com.example.cliente12

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.cliente12.`interface`.AceptarPresupuesto
import com.example.cliente12.clases.ClaseUrl
import com.example.cliente12.clases.modeloFirebase
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.GenericTypeIndicator
import retrofit.client.Response
import retrofit.Callback
import retrofit.RestAdapter
import retrofit.RetrofitError
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception


class MainActivityColeccionFirebase : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    lateinit var respuesta: TextView
    lateinit var btnAcpetarPresupuesto: Button

    val url = ClaseUrl()
    val ROOT_URL = url.ROOT_URL


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_coleccion_firebase)
        database = Firebase.database.reference
        database.addValueEventListener(postListener)

        respuesta = findViewById(R.id.txtRespuesta)

        btnAcpetarPresupuesto = findViewById(R.id.buttonAceptarPresupuesto)
        btnAcpetarPresupuesto.setOnClickListener {
            //Toast.makeText(this, "Presupuesto Aceptado ", Toast.LENGTH_SHORT).show()
           // AceptarP()
            val intent1 = getIntent()
            val folio = intent1.getStringExtra("Folio")

            val intent = Intent(this, MainActivitySubirComprobanteDePAgo::class.java)
           intent.putExtra("Folio", folio.toString())
            startActivity(intent)
        }


    }

    val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
           /* val t: GenericTypeIndicator<List<modeloFirebase?>?> =
                object : GenericTypeIndicator<List<modeloFirebase?>?>() {}
            val messages: List<modeloFirebase?>? = dataSnapshot.getValue(t)
            respuesta.setText(messages.toString())*/
           // dataSnapshot.child("Presupuesto 14").child("1").value
           // val list = ArrayList<String>()

         //   respuesta.setText("${dataSnapshot.child("Presupuesto 14").value}")


            //val folio = "14"
            var con =1
          /* val intent = getIntent()
          database.child("Presupuesto $folio").get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")

               var tam = it.childrenCount
               for (tam in 1 until tam+1){
                   database.child("Presupuesto $folio").child(con.toString()).get()
                   con++
                   respuesta.setText("${it.children}")
               }

                //obtenemos los hijos de la coleccion
               //respuesta.setText("${it.childrenCount}")


            }.addOnFailureListener{
                Log.e("firebase", "Error de estructura", it)
            }*/

            //val post = dataSnapshot.getValue<modeloFirebase>()
           // respuesta.setText("${post?.pago}")


          /*  val key = database.child("Presupuesto 11").push().key
            if (key == null) {
                Log.w(TAG, "no se encontro")
                return
            }
            respuesta.setText("$key")*/
            val intent = getIntent()
            val folio = intent.getStringExtra("Folio")
            val pagoTotal= intent.getStringExtra("PagoTotal")

            val coleccion = dataSnapshot.child("Presupuesto ${folio.toString()}").value as MutableList<*>

                val c = coleccion[con] as HashMap<*, *>
                val d = c.get("descripcion")
            val p = c.get("pago")
                con++
                respuesta.setText("Descripcion ${d.toString()}, pago ${p.toString()}, Pago Total ${pagoTotal.toString()}")
                Log.d("coleccion", d.toString())
            }

        override fun onCancelled(error: DatabaseError) {
           Toast.makeText(this@MainActivityColeccionFirebase, "Hubo un erro $error", Toast.LENGTH_SHORT).show()
        }


    }


    private fun AceptarP (){
        val intent = getIntent()
        val Folio = intent.getStringExtra("Folio")

        val adaptar = RestAdapter.Builder()
            .setEndpoint(ROOT_URL)
            .build()
        val api: AceptarPresupuesto = adaptar.create(AceptarPresupuesto ::class.java)
        api.Aceptar(Folio.toString(),
            "1",

        object : Callback<Response?>{
            override fun success(t: Response?, response: retrofit.client.Response?) {
                var entrada: BufferedReader? =  null
                var Respuesta = ""
                try {
                    entrada = BufferedReader(InputStreamReader(t?.body?.`in`()))
                    Respuesta = entrada.readLine()
                }catch (e: Exception){
                    e.printStackTrace()
                }
                Toast.makeText(this@MainActivityColeccionFirebase, Respuesta, Toast.LENGTH_SHORT).show()
                val intent  = Intent(this@MainActivityColeccionFirebase, MainActivityPagos::class.java)
                startActivity(intent)
            }

            override fun failure(error: RetrofitError?) {
                Toast.makeText(this@MainActivityColeccionFirebase, "error " + error, Toast.LENGTH_SHORT).show()
            }

        })

    }

}