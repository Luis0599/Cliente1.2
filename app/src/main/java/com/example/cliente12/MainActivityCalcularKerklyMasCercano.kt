package com.example.cliente12

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.cliente12.`interface`.InterfacePresupuestoRecibido
import com.example.cliente12.clases.ClaseAdapterPresuR
import com.example.cliente12.clases.ClaseUrl
import com.example.cliente12.clases.modeloP
import com.example.cliente12.recursoParaMejorRutas.ModeloRutas
import com.example.cliente12.recursoParaMejorRutas.interfaceMejorRuta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivityCalcularKerklyMasCercano : AppCompatActivity() {
    lateinit var btnKcercano: Button
    lateinit var txtKcercano: TextView
    var distance =0.00
    var menorDistancia =0.00
    var Curp = ""
    var u = ClaseUrl()
    var url = u.ROOT_URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_calcular_kerkly_mas_cercano)

        txtKcercano = findViewById(R.id.txtKerklyMasCercano)
        btnKcercano = findViewById(R.id.btnKerklyMasCercano)

        btnKcercano.setOnClickListener {
           // Toast.makeText(this, "entro ", Toast.LENGTH_SHORT).show()
           // llenarArray()
            ObetenerCoordenadas()
        }


    }

    private fun ObetenerCoordenadas(){
        val retrofit = Retrofit.Builder()
            .baseUrl(url+"/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val get = retrofit.create(interfaceMejorRuta::class.java)
        val call = get.ObtenerC("Albañil")

        call?.enqueue(object : Callback<List<ModeloRutas?>?> {

            override fun onResponse(call: Call<List<ModeloRutas?>?>, response: Response<List<ModeloRutas?>?>) {

                val poslist: ArrayList<ModeloRutas> = response.body() as ArrayList<ModeloRutas>

                //coordenadas de casa de mane, origen
                val locationA = Location("punto De origen")
                val latA = 17.5624445
                val lngA = -99.5049045
                locationA.latitude = latA
                locationA.longitude = lngA


                val locationB = Location("punto destino")
                Curp = poslist.get(0).getCurp()
                locationB.latitude = poslist.get(0).getLatitud()
                locationB.longitude = poslist.get(0).getLongitud()
                menorDistancia = locationA.distanceTo(locationB).toDouble()
                Log.e("curp $Curp distancia 0", menorDistancia.toString())
                for(i in 1 until poslist.size){
                    val locationC = Location("punto c")
                    Curp = poslist.get(i).getCurp()
                    locationC.latitude = poslist.get(i).getLatitud()
                    locationC.longitude = poslist.get(i).getLongitud()
                    distance = locationA.distanceTo(locationC).toDouble()
                    Log.e("Curp $Curp distancia $i ", "distancia $distance")
                    if (distance < menorDistancia){
                        menorDistancia = distance
                        Curp = poslist.get(i).getCurp()
                        txtKcercano.setText("Curp $Curp menor distancia $menorDistancia" )
                    }
                }

                //txtKcercano.setText("Curp $Curp menor distancia $menorDistancia")
               // Log.e("Curp $Curp distancia ", "menor distancia $menorDistancia")

            }

            override fun onFailure(call: Call<List<ModeloRutas?>?>, t: Throwable) {
                Toast.makeText(this@MainActivityCalcularKerklyMasCercano, t.toString(), Toast.LENGTH_LONG).show()
                System.out.println("el error es: ${t.toString()}")

            }

        })

    }

/*fun llenarArray(){

  /*  // var origen: Array<Location?>
     val myArray  = arrayListOf(17.5364664, -99.4958669, 17.543049, -99.5188493)
     //coordenadas de mi casa, origen
     val locationA = Location("punto De origen")
     val latA = 16.9449045
     val lngA = -98.2387563
     locationA.latitude = latA
     locationA.longitude = lngA
     var distance =0.00


     val locationB = Location("puntos de kerkly")
     var latB = myArray.get(0)
     var lngB = myArray.get(1)
     locationB.latitude = latB
     locationB.longitude = lngB
     distance = locationA.distanceTo(locationB).toDouble()
     for(i in 0..myArray.size){

     }

    /* val locationB = Location("puntos de kerkly")
     val latB = 16.944905000000002//myArray.get(2)
     val lngB = -98.23882833333333//myArray.get(3)
     locationB.latitude = latB
     locationB.longitude = lngB

     distance = locationA.distanceTo(locationB).toDouble()*/
     txtKcercano.setText("distancia entre la escuela y mi casita en metro $distance ")*/


     var modelo = ModeloRutas()
     //ruta de la facultad 148.81 km
     modelo.latitud = 17.5364664
     modelo.longitud = -99.4958669

     val modelo2 = ModeloRutas()
     // ruta de la colonia san pedro 151.32 kilometros de mi casa a esa colonia
     modelo2.latitud = 17.543049
     modelo2.longitud = -99.5188493

     val modelo3 = ModeloRutas()
     //el carmen
     modelo3.latitud = 16.9449045
     modelo3.longitud = -98.2387563


     //coordenadas de casa de mane, origen
     val locationA = Location("punto De origen")
     val latA = 17.5624445
     val lngA = -99.5049045
     locationA.latitude = latA
     locationA.longitude = lngA
     var distance =0.00
     var menorDistancia =0.00
     var myList = ArrayList<ModeloRutas>()
     myList.add(modelo)
     myList.add(modelo2)
      myList.add(modelo3)
     val locationB = Location("punto destino")
     locationB.latitude = myList.get(0).latitud
     locationB.longitude = myList.get(0).longitud
     menorDistancia = locationA.distanceTo(locationB).toDouble()
     Log.e("distancia 0", menorDistancia.toString())
    for(i in 1 until myList.size){
         val locationC = Location("punto c")
         locationC.latitude = myList.get(i).latitud
         locationC.longitude = myList.get(i).longitud
         distance = locationA.distanceTo(locationC).toDouble()
         Log.e("distancia $i ", "distancia $distance")
         if (distance < menorDistancia){
             menorDistancia = distance
            // txtKcercano.setText("menor distancia $distance" )
         }
     }

     txtKcercano.setText("menor distancia $menorDistancia")
 }*/

}