package com.example.cliente12


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cliente12.`interface`.InterfacePresupuestoRecibido
import com.example.cliente12.clases.ClaseAdapterPresuR
import com.example.cliente12.clases.ClaseUrl
import com.example.cliente12.clases.modeloP
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivityRecibirPresupuesto : AppCompatActivity() {
    var u = ClaseUrl()
    var url = u.ROOT_URL

    lateinit var MiAdapter: ClaseAdapterPresuR
    lateinit var recyclerview: RecyclerView

    lateinit var progressBar: ProgressBar
    lateinit var dialog: Dialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_recibir_presupuesto)
        recyclerview= findViewById(R.id.Mirecycler)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager= LinearLayoutManager(this@MainActivityRecibirPresupuesto)
        setProgressDialog()
        mostrar()

    }

    fun mostrar(){
        val intent = getIntent()
        val numeroT = intent.getStringExtra("numero")

        val retrofit = Retrofit.Builder()
            .baseUrl(url+"/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val get = retrofit.create(InterfacePresupuestoRecibido::class.java)
        val call = get.EnviarT(numeroT.toString())

        call?.enqueue(object : Callback<List<modeloP?>?> {

            override fun onResponse(call: Call<List<modeloP?>?>, response: Response<List<modeloP?>?>) {

                val poslist: ArrayList<modeloP> = response.body() as ArrayList<modeloP>
                MiAdapter = ClaseAdapterPresuR(poslist)
                MiAdapter.setOnClickListener {
                    val folio = poslist.get(recyclerview.getChildAdapterPosition(it)).getIdPresupuestoNoRegistrado()
                    val PagoTotal = poslist.get(recyclerview.getChildAdapterPosition(it)).getPagoTotal()

                //esto esta comentando para hacer probar otra manera de calcular rutas
                  var  intent = Intent(this@MainActivityRecibirPresupuesto, MainActivityColeccionFirebase::class.java)
                    intent.putExtra("Folio", folio.toString())
                    intent.putExtra("PagoTotal", PagoTotal.toString())
                    startActivity(intent)

                   /*var intent2 =Intent(this@MainActivityRecibirPresupuesto, MapsActivityTrazarMejorRuta::class.java)
                    startActivity(intent2)*/

                }
                recyclerview.adapter = MiAdapter
                dialog.dismiss()
                System.out.println("el numero es: ${numeroT.toString()}")
            }

            override fun onFailure(call: Call<List<modeloP?>?>, t: Throwable) {
                Toast.makeText(this@MainActivityRecibirPresupuesto, t.toString(), Toast.LENGTH_LONG).show()
                System.out.println("el error es: ${t.toString()}")
                dialog.dismiss()
            }

        })

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

        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
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
}