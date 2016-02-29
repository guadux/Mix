package com.invixion.coches2015;


import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Clase que representa la existencia de un Coche
 */
public class Coche {
    private String nombre;
    private String imagen;


    public Coche(String nombre, String img) {
        this.nombre = nombre;
        this.imagen = img;
    }

    public String getNombre() {
        return nombre;
    }


    public String getImg() {
        return imagen;
    }

    public int getId() {
        return nombre.hashCode();
    }

    private void llenarItems(){

    }

    public static void appendCoche(Coche newObj) {

        for(int i=0; i < Coche.ITEMS.length; i++){
            if(ITEMS[i] == null){
                ITEMS[i] = newObj;
                System.out.println("se agrego uno");
            }
        }
        System.out.println("despues del append la cantidad es: "+ITEMS.length);

    }

    public static Coche[] ITEMS = {
            new Coche("Jaguar F-Type 2015", "http://br.guiainfantil.com/uploads/educacao/casaCG.jpg"),
//            new Coche("Mercedes AMG-GT", R.drawable.mercedes_benz_amg_gt),
//            new Coche("Mazda MX-5", R.drawable.mazda_mx5_2015),
//            new Coche("Porsche 911 GTS", R.drawable.porsche_911_gts),
//            new Coche("BMW Serie 6", R.drawable.bmw_serie6_cabrio_2015),
//            new Coche("Ford Mondeo", R.drawable.ford_mondeo),
//            new Coche("Volvo V60 Cross Country", R.drawable.volvo_v60_crosscountry),
//            new Coche("Jaguar XE", R.drawable.jaguar_xe),
//            new Coche("VW Golf R Variant", R.drawable.volkswagen_golf_r_variant_2015),
//            new Coche("Seat León ST Cupra", R.drawable.seat_leon_st_cupra),
    };

    /**
     * Obtiene item basado en su identificador
     *
     * @param id identificador
     * @return Coche
     */
    public static Coche getItem(int id) {
        for (Coche item : ITEMS) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
