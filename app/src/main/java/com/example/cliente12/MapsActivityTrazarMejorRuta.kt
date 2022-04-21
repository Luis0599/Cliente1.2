package com.example.cliente12

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.cliente12.databinding.ActivityMapsTrazarMejorRutaBinding
import com.example.cliente12.recursoTrazarLinea.GPSTracker
import com.example.cliente12.recursoTrazarLinea.GeoTask
import com.example.cliente12.recursoTrazarLinea.Utils1
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps_trazar_mejor_ruta.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class MapsActivityTrazarMejorRuta() : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
    GoogleMap.OnMarkerClickListener,  GeoTask.Geo {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsTrazarMejorRutaBinding
    private var locationManager: LocationManager? = null
     var latitud: Double = 0.0
     var longitud: Double = 0.0
    lateinit var context: Context
    var tipo = 1
    lateinit var mapa: String

    var request: RequestQueue? = null
    var progressBar: ProgressBar? = null
    var location: Location? = null
    var gpsTracker: GPSTracker? = null
    private val pop: View? = null
    lateinit var dialog: Dialog
    var jsonObjectRequest: JsonObjectRequest? = null
    var marker1: Marker?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressBar = findViewById(R.id.progress_circular)
        gpsTracker = GPSTracker(applicationContext)
        location = gpsTracker!!.location
        request = Volley.newRequestQueue(applicationContext)

        binding = ActivityMapsTrazarMejorRutaBinding.inflate(layoutInflater)
        setContentView(binding.root)
         context = this
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getLocalizacion()


        button2.setOnClickListener {
            when(tipo) {
                1 -> Hibrido()
                2 -> satelite()
                3 -> terreno()
                4 -> terreno2()
                else-> print(" ")
            }
        }


    }

    @SuppressLint("MissingPermission")
    private fun getLocalizacion() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        //En caso de que el Gps este desactivado, entrara en el if y nos mandara a la configuracion de nuestro Gps para activarlo
        //En caso de que el Gps este desactivado, entrara en el if y nos mandara a la configuracion de nuestro Gps para activarlo

        if (!gpsEnabled) {
            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(settingsIntent)
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)
            return
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true

        val locationManager = this@MapsActivityTrazarMejorRuta.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
               val miUbicacion = LatLng(location.getLatitude(), location.getLongitude())
                latitud = location.latitude
                longitud = location.longitude
                locationManager.removeUpdates(this)
                mMap.setMyLocationEnabled(true)
                mMap.isMyLocationEnabled = true
                mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion))
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                val cameraPosition = CameraPosition.Builder()
                    .target(miUbicacion)
                    .zoom(15f)
                    .bearing(90f)
                    .tilt(45f)
                    .build()
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))


            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                when (status) {
                    LocationProvider.AVAILABLE -> Log.d("debug", "LocationProvider.AVAILABLE")
                    LocationProvider.OUT_OF_SERVICE -> Log.d("debug", "LocationProvider.OUT_OF_SERVICE")
                    LocationProvider.TEMPORARILY_UNAVAILABLE -> Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE")
                }
            }
            override fun onProviderEnabled(provider: String) {
                Toast.makeText(context, "GPS activado", Toast.LENGTH_SHORT).show()
            }
            override fun onProviderDisabled(provider: String) {
                Toast.makeText(context, "GPS Desactivado", Toast.LENGTH_SHORT).show()
            }
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)

        //los marcadores
        Utils1.markersDefault(mMap, applicationContext)
        mMap!!.setOnMapLongClickListener(this)
        mMap!!.setOnMarkerClickListener(this)

    }


    private fun Hibrido() {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mapa = "HÍBRIDO";
        tipo = 2;
    }
    private fun satelite() {
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa = "SATÉLITE";
        tipo =3;
    }
    private fun terreno() {
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mapa = "TERRENO";
        tipo = 4;
    }
    private fun terreno2() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa = "TERRENO";
        tipo = 1;
    }

    override fun onMapLongClick(latLng: LatLng) {
        Utils1.coordenadas.origenLat = latLng.latitude
        Utils1.coordenadas.origenLongi = latLng.longitude
       // Toast.makeText(this, "Toque icono donde sea dirigir", Toast.LENGTH_SHORT).show()

    }

    override fun onMarkerClick(marker: Marker): Boolean {
        AlertShow(marker.title, marker.position)
        //Toast.makeText(context, "${marker.position}", Toast.LENGTH_LONG).show()
        return false
    }


    fun AlertShow(title: String?, latLng: LatLng) {
        val builder = AlertDialog.Builder(this@MapsActivityTrazarMejorRuta)
        builder.setMessage("Desea ir este punto?")
        builder.setTitle(title)
        builder.setCancelable(false)
        builder.setPositiveButton("Si") { dialog, which ->
           Toast.makeText(this, "elegido", Toast.LENGTH_LONG).show()
            TrazarLineas(latLng)
        }
        builder.setNegativeButton("No") { dialog, which -> dialog.cancel() }
        val alertDialog = builder.create()
        alertDialog.show()

    }

    fun setProgressDialog() {
        val llPadding = 30
        val ll = LinearLayout(this)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam
        progressBar = ProgressBar(this)

        progressBar!!.isIndeterminate = true
        progressBar!!.setPadding(0, 0, llPadding, 0)
        progressBar!!.layoutParams = llParam
        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(this)
        tvText.text = "Por favor espere un momento..."
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = llParam
        ll.addView(progressBar)
        ll.addView(tvText)
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(ll)
        dialog = builder.create()
        dialog.show()
        val window: Window? = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window!!.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window!!.attributes = layoutParams
        }
    }


    fun ObtenerRuta(latInicial: String, lngInicial: String, latFinal: String, lngFinal: String) {
        val url = "https://maps.googleapis.com/maps/api/directions/json?origin=$latInicial,$lngInicial&destination=$latFinal,$lngFinal&key=AIzaSyAp-2jznuGLfRJ_en09y1sp6A-467zrXm0&mode=drive"
        //Toast.makeText(context, "inicial $latInicial, $lngInicial final $latFinal, $lngFinal", Toast.LENGTH_SHORT).show()
     //   textView4.setText("inicial $latInicial, $lngInicial final $latFinal, $lngFinal")
        jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            var jRoutes: JSONArray? = null
            var duracion: JSONArray?
            var jLegs: JSONArray? = null
            var jSteps: JSONArray? = null
            try {
                jRoutes = response.getJSONArray("routes")
               // duracion = response.getJSONArray("duration")
               // Toast.makeText(this, "hola ${jRoutes.length()}", Toast.LENGTH_LONG).show()
               // Toast.makeText(this@MapsActivityTrazarMejorRuta, response.toString(), Toast.LENGTH_SHORT).show()
               //textView4.setText(duracion.toString())
                for (i in 0 until jRoutes.length()) {
                    jLegs = (jRoutes[i] as JSONObject).getJSONArray("legs")
                    val path: MutableList<HashMap<String, String>> = ArrayList()
                    for (j in 0 until jLegs.length()) {
                        jSteps = (jLegs[j] as JSONObject).getJSONArray("steps")
                        for (k in 0 until jSteps.length()) {
                            var polyline = ""
                            polyline = ((jSteps[k] as JSONObject)["polyline"] as JSONObject)["points"] as String
                            val list = decodePoly(polyline)
                            for (l in list.indices) {
                                val hm = HashMap<String, String>()
                                hm["lat"] = java.lang.Double.toString(list[l].latitude)
                                hm["lng"] = java.lang.Double.toString(list[l].longitude)
                                path.add(hm)

                            }
                        }

                        Utils1.routes.add(path)
                        //Toast.makeText(this, "hola", Toast.LENGTH_LONG).show()
                        Utils1.markersDefault(mMap, applicationContext)
                        var points: ArrayList<LatLng?>? = null
                        var lineOptions: PolylineOptions? = null

                        for (i in 0 until Utils1.routes.size) {
                            points = ArrayList()
                            lineOptions = PolylineOptions()
                            // Obteniendo el detalle de la ruta
                            val path: List<HashMap<String, String>> = Utils1.routes.get(i)
                            for (j in path.indices) {
                                val point = path[j]
                                val lat = Objects.requireNonNull(point["lat"])!!.toDouble()
                                val lng = Objects.requireNonNull(point["lng"])!!.toDouble()
                                val position = LatLng(lat, lng)
                                points.add(position)
                            }
                            lineOptions.addAll(points)
                            lineOptions.width(9f)
                            //Definimos el color de la Polilíneas
                            lineOptions.color(Color.BLUE)
                        }
                        assert(lineOptions != null)
                        mMap.addPolyline(lineOptions!!)
                        dialog.dismiss()


                    }
                }


            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: java.lang.Exception) {
            }
        }
        ) { error ->
            Toast.makeText(applicationContext, "No se puede conectar $error", Toast.LENGTH_LONG)
                .show()
            println()
            Log.d("ERROR: ", error.toString())
        }
        request!!.add(jsonObjectRequest)

        val url2 = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=$latInicial,$lngInicial&destinations=$latFinal,$lngFinal&mode=driving&language=fr-FR&avoid=tolls&key=AIzaSyAp-2jznuGLfRJ_en09y1sp6A-467zrXm0";
        textView4.setText("entro")
        GeoTask(context).execute(url2)


    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly: MutableList<LatLng> = ArrayList()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }
        return poly
    }

    fun TrazarLineas(latLng: LatLng){
        Utils1.coordenadas.destinoLat = latLng.latitude
        Utils1.coordenadas.destinoLng = latLng.longitude
        setProgressDialog()
        try {
            while (location == null) {
                location = gpsTracker!!.location

            }
            location = gpsTracker!!.location
            Utils1.coordenadas.origenLat = location!!.latitude
            Utils1.coordenadas.origenLongi = location!!.longitude
            Log.d("latitud", location!!.latitude.toString())
            Log.d("longitud", location!!.longitude.toString())
            ObtenerRuta(
                Utils1.coordenadas.origenLat.toString(),
                Utils1.coordenadas.origenLongi.toString(),
                Utils1.coordenadas.destinoLat.toString(),
                Utils1.coordenadas.destinoLng.toString()
            )


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun setDouble(result: String?) {
        val res = result!!.split(",").toTypedArray()
        val min = res[0].toDouble() / 60
        val dist = res[1].toInt() / 1000

        textView_result1.setText("Duracion= " + (min / 60).toInt() + " hr " + (min % 60).toInt() + " mins")
        textView_result2.setText("Distancia= $dist kilometros")
    }

}