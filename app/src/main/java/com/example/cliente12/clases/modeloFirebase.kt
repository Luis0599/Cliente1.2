package com.example.cliente12.clases

import com.google.firebase.database.Exclude


 class modeloFirebase(id: Int?, descripcion: String?, pago: String?) {

    var id: Int? =id
    var descripcion: String? =descripcion
    var pago: String?  = pago




     fun modeloFirebase(id: Int?, descripcion: String?, pago: String?) {
         this.id = id
         this.descripcion = descripcion
         this.pago = pago

     }


     fun getId(): Int { return id!!
     }

     fun setId(i: Int) {
         id = i
     }

     @JvmName("getDescripcion1")
     fun getDescripcion(): String?{

         return descripcion
     }

     @JvmName("setDescripcion1")
     fun setDescripcion(d: String?){
         descripcion = d
     }

     @JvmName("getPago1")
     fun getPago(): String?{
         return pago
     }
     @JvmName("setPago1")
     fun setPago(p: String){
         pago = p
     }



}