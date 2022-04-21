package com.example.cliente12


import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import android.os.Environment
import android.os.Build
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import java.io.*
import java.lang.Integer.min
import java.text.SimpleDateFormat
import java.util.*

import android.widget.TextView

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Handler
import java.lang.Exception



class MainActivitySubirComprobanteDePAgo : AppCompatActivity() {
    lateinit var btnSeleccionarFoto: Button
    lateinit var btnTomarFoto: Button
    lateinit var txtMensaje: TextView
    lateinit var storageReference: StorageReference
    lateinit var referenceImage: StorageReference
    lateinit var storage: FirebaseStorage
    private val select_picture = 400
    private lateinit var U: Uri
    lateinit var imagen:  ImageView

    val REQUEST_IMAGE_CAPTURE = 1
    val  REQUEST_TAKE_PHOTO = 1
    lateinit var currentPhotoPath: String

    lateinit var progressBar: ProgressBar
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_subir_comprobante_de_pago)


        txtMensaje = findViewById(R.id.txtmensaje)
        storageReference = FirebaseStorage.getInstance().reference

        storage = storageReference.storage
       // progressBar = findViewById(R.id.progressbar);
       // progressBar.setVisibility(View.INVISIBLE);
        imagen = findViewById(R.id.imageViewFoto)
        btnSeleccionarFoto = findViewById(R.id.buttonGaleria)

        btnSeleccionarFoto.setOnClickListener {
           // Toast.makeText(this, "entro", )
            val opcion = arrayOf<CharSequence>("Elegir de galeria", "cancelar")
            val builder = AlertDialog.Builder(this@MainActivitySubirComprobanteDePAgo)
            builder.setTitle("Elije una Opcion ")
            builder.setItems(
                opcion
            ) { dialog, posicion ->
                if (opcion[posicion] === "Elegir de galeria") {
                    cargarimagen()
                } else {
                    if (opcion[posicion] === "cancelar") {
                        dialog.dismiss()
                    }
                }
            }
            builder.show()
        }


        btnTomarFoto = findViewById(R.id.buttonTomarFoto)
        btnTomarFoto.setOnClickListener {
          // dispatchTakePictureIntent()

            metodoParaSigActiviy()
        }

    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()

                } catch (ex: IOException) {
                    // Error occurred while creating the File
                  null
                }

                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.cliente12",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                    U = photoURI

                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }



    private fun cargarimagen() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Selecciona la imagen"), select_picture)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
     /*   updateBarHandler = Handler()
        progressDialog = ProgressDialog(this@MainActivitySubirComprobanteDePAgo)
        progressDialog.setTitle("Procesando")
        progressDialog.setMessage("Por favor espere ...")
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.setCancelable(false);
        progressDialog.setProgress(0)
        progressDialog.setMax(100)*/



        //obtenemos el folio
        val intent1 = getIntent()
        val folio = intent1.getStringExtra("Folio")
            //codigo para guardar la foto tomada por nuestra camara
       if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
          setProgressDialog()

           referenceImage = storageReference.child("Comprobante ${folio.toString()}")
           referenceImage.putFile(U).addOnSuccessListener(this,
                 OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->

                     val targetW: Int = imagen.width
                     val targetH: Int = imagen.height

                     val bmOptions = BitmapFactory.Options().apply {
                         // Get the dimensions of the bitmap
                         inJustDecodeBounds = true
                         val photoW: Int = outWidth
                         val photoH: Int = outHeight
                         val scaleFactor: Int = min(photoW / targetW, photoH / targetH)
                         inJustDecodeBounds = false
                         inSampleSize = scaleFactor
                     }
                     BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
                         imagen.setImageBitmap(bitmap)
                         dialog.dismiss()
                         metodoParaSigActiviy()
                     }


                 })

          /* progressDialog.show()
           Thread {
               for (i in 0..progressDialog.getMax()) {
                   //tarea a realizar
                   //trabajando()
                   //codigo para mandar la foto a firebase
                   referenceImage = storageReference.child("Comprobante ${folio.toString()}")
                   /*  referenceImage.putFile(U).addOnSuccessListener(this,
                         OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->

                             val targetW: Int = imagen.width
                             val targetH: Int = imagen.height

                             val bmOptions = BitmapFactory.Options().apply {
                                 // Get the dimensions of the bitmap
                                 inJustDecodeBounds = true
                                 val photoW: Int = outWidth
                                 val photoH: Int = outHeight
                                 val scaleFactor: Int = min(photoW / targetW, photoH / targetH)
                                 inJustDecodeBounds = false
                                 inSampleSize = scaleFactor
                             }
                             BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
                                 imagen.setImageBitmap(bitmap)
                             }


                         })*/

                   /* referenceImage.putFile(U).addOnSuccessListener {
                        val storageRef = storage.reference




                    }.addOnProgressListener { taskSnapshot ->
                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "%...")
                        Thread.sleep(progress.toLong())

                    }*/

                   referenceImage.putFile(U).addOnSuccessListener (this,
                   OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                       Toast.makeText(this, "ya se envio", Toast.LENGTH_SHORT).show()
                       val bmOptions = BitmapFactory.Options().apply {
                           // Get the dimensions of the bitmap
                           inJustDecodeBounds = true
                           val photoW: Int = outWidth
                           val photoH: Int = outHeight
                           //val scaleFactor: Int = min(photoW / targetW, photoH / targetH)
                           inJustDecodeBounds = false
                           //  inSampleSize = scaleFactor
                       }

                       BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
                           imagen.setImageBitmap(bitmap)
                       }
                      // Toast.makeText(this, "todo bien", Toast.LENGTH_SHORT).show()
                   }).addOnProgressListener { taskSnapshot ->
                        long = ((100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount))
                      // progressDialog.setMessage("Uploaded " + progress.toInt() + "%...")
                       Thread.sleep(long.toLong())

                   }

               }

               txtMensaje.setText("${long.toString()}")
                   /*  Toast.makeText(this, "Comprobante enviado ", Toast.LENGTH_SHORT).show()
                     txtMensaje.setText(U.toString())
                     galleryAddPic()*/
                   progressDialog.dismiss()
                   }.start()
                   */
               }else{

               if (resultCode == RESULT_OK) {
                   setProgressDialog()
                   U = data!!.data!!
                   // imagen.setImageURI(U)
                   referenceImage = storageReference.child("Comprobante ${folio.toString()}")
                   referenceImage.putFile(U).addOnSuccessListener(this,
                       OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                           /* val uriTask = taskSnapshot.storage.downloadUrl
                            while (!uriTask.isSuccessful);
                            val DescargarUrl = uriTask.result
                            urlImage = DescargarUrl!!*/
                           imagen.setImageURI(U)
                           txtMensaje.setText(U.toString())

                           dialog.dismiss()
                           Thread.sleep(500)
                           //Toast.makeText(this, "Comprobante enviado", Toast.LENGTH_SHORT).show()
                           metodoParaSigActiviy()
                       })
               }
           }
    }





    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }


    private fun trabajando() {
        try {
            Thread.sleep((Math.floor(Math.random() * (9000) + 9000)  as Int).toLong())
        } catch (ex: Exception) {
        }
    }
//codigo para mostrar que se esta subiendo la foto

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
        tvText.text = "Subiendo..."
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

    private fun metodoParaSigActiviy(){
       val intent = Intent(this, MainActivityCalcularKerklyMasCercano::class.java)
        startActivity(intent)
    }
}


