package com.example.cliente12.recursoTrazarLinea

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import android.graphics.drawable.BitmapDrawable
import com.example.cliente12.R
import android.graphics.Bitmap
import android.location.Location
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.cliente12.clases.ClaseUrl
import com.example.cliente12.recursoParaMejorRutas.ModeloRutas
import com.example.cliente12.recursoParaMejorRutas.interfaceMejorRuta
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MarkersDefault(var nMap: GoogleMap, var context: Context) {
    var Curp = ""
    var u = ClaseUrl()
    var url = u.ROOT_URL
    var poslist: ArrayList<ModeloRutas>? =null

   /* fun addMarkersDefault() {
        ObetenerCoordenadasBaseDeDatos()

        uno(17.543049, -99.5188493, "punto 1")
        dos(17.5364664, -99.4958669, "punto 2")
    }*/

    fun CrearMarcador(location: Location, num: Int) {
        val punto = LatLng(location.latitude, location.longitude)
        val height = 60
        val width = 65
        val jira = context.resources.getDrawable(R.drawable.luis) as BitmapDrawable
        val ji = jira.bitmap
        val jiras = Bitmap.createScaledBitmap(ji, width, height, false)
        nMap.addMarker(
            MarkerOptions()
                .position(punto)
                .title("punto $num").snippet("desea ir este punto?")
                .icon(BitmapDescriptorFactory.fromBitmap(jiras))
        )
    }



     fun ObetenerCoordenadasBaseDeDatos(){
        val retrofit = Retrofit.Builder()
            .baseUrl(url+"/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val get = retrofit.create(interfaceMejorRuta::class.java)
        val call = get.ObtenerC("Albañil")

        call?.enqueue(object : Callback<List<ModeloRutas?>?> {

            override fun onResponse(call: Call<List<ModeloRutas?>?>, response: Response<List<ModeloRutas?>?>) {

                poslist = response.body() as ArrayList<ModeloRutas>
                //Log.e("tamaño ", "${poslist!!.size}")
                val location1 = Location("punto 1")
                for(i in 0 until poslist!!.size){
                location1.latitude = poslist!!.get(i).getLatitud()
                location1.longitude = poslist!!.get(i).getLongitud()
                    CrearMarcador(location1, i+1)
                    System.out.println("entro " + i+1)
                }

            }

            override fun onFailure(call: Call<List<ModeloRutas?>?>, t: Throwable) {
                //Toast.makeText(this, t.toString(), Toast.LENGTH_LONG).show()
                System.out.println("el error es: ${t.toString()}")

            }

        })

    }
}