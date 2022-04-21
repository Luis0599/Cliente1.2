package com.example.cliente12.recursoTrazarLinea

class Coordenadas {
    var origenLat: Double = 0.0
    var origenLongi: Double = 0.0
    var destinoLat: Double = 0.0
    var destinoLng: Double = 0.0

    fun getOrigenLat(): Double? {
        return origenLat
    }

    fun setOrigenLat(origenLat: Double?) {
        this.origenLat = origenLat!!
    }

    fun getOriggenLng(): Double? {
        return origenLongi
    }

    fun setOriggenLng(origgenLng: Double?) {
        this.origenLongi = origgenLng!!
    }

    fun getDestinoLat(): Double? {
        return destinoLat
    }

    fun setDestinoLat(destinoLat: Double?) {
        this.destinoLat = destinoLat!!
    }

    fun getDestinoLng(): Double? {
        return destinoLng
    }

    fun setDestinoLng(destinoLng: Double?) {
        this.destinoLng = destinoLng!!
    }
}