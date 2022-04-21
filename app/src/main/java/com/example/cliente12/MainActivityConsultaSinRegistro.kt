package com.example.cliente12

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.cliente12.`interface`.direccionCoordenadas
import com.example.cliente12.clases.ClaseUrl
import retrofit.Callback
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

class MainActivityConsultaSinRegistro : AppCompatActivity() {
    lateinit var BotonGPS: Button
    lateinit var botonConfirmar: Button

    lateinit var txtCiudad: EditText
    lateinit var txtEstado: EditText
    lateinit var txtPais: EditText
    lateinit var txtCalle: EditText
    lateinit var txtColonia: EditText
    lateinit var txtNoInterior: EditText
    lateinit var txtNoExterior: EditText
    lateinit var txtCodigoPostal: EditText
    lateinit var txtlatitud: TextView
    lateinit var txtLongitud: TextView
    lateinit var txtDireccion: TextView


    var url = ClaseUrl()
    val ROOT_URL = url.ROOT_URL
    private var locationManager: LocationManager? = null

    lateinit var spinerR: Spinner;
    lateinit var problema: EditText
    lateinit var fechaH:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_consulta_sin_registro)



        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?;

        txtCiudad = findViewById(R.id.editTextCiudad)
        txtEstado = findViewById(R.id.editTextEstado)
        txtPais = findViewById(R.id.editTextPais)
        txtCalle = findViewById(R.id.editTextCalle)
        txtColonia = findViewById(R.id.editTextColonia)
        txtNoInterior = findViewById(R.id.editTextNoInterior)
        txtNoExterior = findViewById(R.id.editTextNoExterior)
        txtCodigoPostal = findViewById(R.id.editTextCodigoPostal)
        txtlatitud = findViewById(R.id.textViewLatitud)
        txtLongitud = findViewById(R.id.textViewLongitud)
        txtDireccion = findViewById(R.id.txtDireccion)
        BotonGPS = findViewById(R.id.buttonGps)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)
        }


        BotonGPS.setOnClickListener {
            locationStart();
            // ObtenerDatos()

        }


        botonConfirmar = findViewById(R.id.buttonConfirmar)
        botonConfirmar.setOnClickListener {
            RegistrarDireccion()


        }

        problema = findViewById(R.id.editTextProblema)
        fechaH = findViewById(R.id.txtFechaYHora)

        spinerR = findViewById(R.id.spinnerOficios)
        val opciones = arrayOf("Electricista","Plomero", "Mecánico", "Herrero", "Albañil", "Carpintero", "Niñera", "Trabajo doméstico")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        spinerR.setAdapter(adapter);

    }


    private fun RegistrarDireccion() {
        //obtener la seleccion del spinner
        val seleccion: Int
        seleccion = spinerR.selectedItemPosition+1
        //obtener fehca y hora actual
        val format = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val fechaHora = format.format(Date())
        fechaH.setText(fechaHora)
        //obtenemos el numero que se ingreso previamente
        val intent = getIntent()
        val numeroT = intent.getStringExtra("numSinRegistro")
        val nombre = intent.getStringExtra("nombreN")
        val apellidoP = intent.getStringExtra("apellidoP")
        val apellidoM = intent.getStringExtra("apellidoM")
        val adapter = RestAdapter.Builder()
            .setEndpoint(ROOT_URL)
            .build()
        val api: direccionCoordenadas = adapter.create(direccionCoordenadas::class.java)
        api.sinRegistro(seleccion, problema.text.toString(),
            txtlatitud.text.toString(),
            txtLongitud.text.toString(),
            txtCalle.text.toString(),
            txtColonia.text.toString(),
            txtNoInterior.text.toString(),
            txtNoExterior.text.toString(),
            txtCiudad.text.toString(),
            txtEstado.text.toString(),
            txtPais.text.toString(),
            txtCodigoPostal.text.toString(),
            "esto es la referencia",
            numeroT.toString(),
            nombre.toString(),
            apellidoP.toString(),
            apellidoM.toString(),

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
                    Toast.makeText(this@MainActivityConsultaSinRegistro, entrada.toString(), Toast.LENGTH_SHORT).show()
                    entrarRecibirPresupuesto()
                }

                override fun failure(error: RetrofitError?) {
                    println("error$error")

                }

            })
    }

    @SuppressLint("MissingPermission")
    private fun locationStart() {

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val Local = Localizacion()
        Local.mainActivityConsultaSinRegistro= this
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
        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, (Local as LocationListener)!!)
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, (Local as LocationListener)!!)
        // println("si entro aqui")


    }



    fun setLocation(loc: Location) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        /*if (loc.latitude !== 0.0 && loc.longitude !== 0.0) {
            try {
                val geocoder = Geocoder(this, Locale.getDefault())
                val list = geocoder.getFromLocation(
                        loc.latitude, loc.longitude, 1)
                if (!list.isEmpty()) {
                    val DirCalle = list[0]

                    txtDireccion.setText(DirCalle.getAddressLine(0))
                    editexDirec.setText(DirCalle.getAddressLine(0))
                    //locationManager!!.removeUpdates((locationManager as LocationListener?)!!)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }*/
        if (loc.latitude !== 0.0 && loc.longitude !== 0.0) {
            try {
                val geocoder: Geocoder
                val direccion: List<Address>
                geocoder = Geocoder(this, Locale.getDefault())

                direccion = geocoder.getFromLocation(loc.latitude, loc.longitude, 1) // 1 representa la cantidad de resultados a obtener
                val address = direccion[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                val ciudad = direccion[0].locality // ciudad
                val estado = direccion[0].adminArea //estado
                val pais = direccion[0].countryName // pais
                val codigoPostal = direccion[0].postalCode //codigo Postal
                val calle = direccion[0].thoroughfare // la calle
                val colonia =  direccion[0].subLocality// colonia
                val numExterior = direccion[0].subThoroughfare

                txtDireccion.setText(address)
                txtCiudad.setText(ciudad)
                txtEstado.setText(estado)
                txtPais.setText(pais)
                txtCalle.setText(calle)
                txtColonia.setText(colonia)
                txtNoExterior.setText(numExterior)
                txtCodigoPostal.setText(codigoPostal)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        //  editexDirec.setText("ciudad $city \n Estado  $state \n pais $country \n codigo Postal $postalCode \n calle $calle \n colonia $colonia")

    }


    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }


    class Localizacion : LocationListener {
        var mainActivityConsultaSinRegistro: MainActivityConsultaSinRegistro? = null



        @SuppressLint("MissingPermission")
        override fun onLocationChanged(loc: Location) {
            // if(loc != null){
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            loc.latitude
            loc.longitude
            val sLatitud = java.lang.String.valueOf(loc.latitude)
            val sLongitud = java.lang.String.valueOf(loc.longitude)
            mainActivityConsultaSinRegistro?.txtlatitud?.setText(sLatitud)
            mainActivityConsultaSinRegistro?.txtLongitud?.setText(sLongitud)
            mainActivityConsultaSinRegistro?.setLocation(loc)
            mainActivityConsultaSinRegistro?.locationManager?.removeUpdates(this)

            //  }

            //mainActivityConsultaSinRegistro?.locationManager!!.removeUpdates((mainActivityConsultaSinRegistro?.locationManager as LocationListener?)!!)
        }

        override fun onProviderDisabled(provider: String) {
            //mainActivityConsultaSinRegistro?.txt.setText("GPS Desactivado")
            Toast.makeText(mainActivityConsultaSinRegistro, "GPS Desactivado", Toast.LENGTH_SHORT).show()
        }

        override fun onProviderEnabled(provider: String) {

            Toast.makeText(mainActivityConsultaSinRegistro, "GPS activado", Toast.LENGTH_SHORT).show()
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            when (status) {
                LocationProvider.AVAILABLE -> Log.d("debug", "LocationProvider.AVAILABLE")
                LocationProvider.OUT_OF_SERVICE -> Log.d("debug", "LocationProvider.OUT_OF_SERVICE")
                LocationProvider.TEMPORARILY_UNAVAILABLE -> Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE")
            }
        }



    }


    fun entrarRecibirPresupuesto(){
        val intent2 = Intent(this, MainActivityRecibirPresupuesto::class.java)
        startActivity(intent2)
    }
}