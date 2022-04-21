package com.example.cliente12.recursoTrazarLinea;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils1 {
    public static Coordenadas coordenadas = new Coordenadas();
    public static List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();

    public static void markersDefault(GoogleMap nMap, Context context){
        new MarkersDefault(nMap, context).ObetenerCoordenadasBaseDeDatos();
    }
}
