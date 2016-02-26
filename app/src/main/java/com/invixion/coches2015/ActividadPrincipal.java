package com.invixion.coches2015;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ActividadPrincipal extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GridView gridView;
    private AdaptadorDeCoches adaptador;

    /*
   Cliente para la conexión al servidor
    */
    HttpURLConnection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);

        usarToolbar();

        gridView = (GridView) findViewById(R.id.grid);
        adaptador = new AdaptadorDeCoches(this);

        gridView.setAdapter(adaptador);
        gridView.setOnItemClickListener(this);

         /*
        Comprobar la disponibilidad de la Red
         */
        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                new GetCommentsTask().
                        execute(
                                new URL("http://invixion.com/listado_medicos.php"));
            } else {
                Toast.makeText(this, "Error de conexion", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {

            e.printStackTrace();
        }

    }

    private void usarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Coche item = (Coche) parent.getItemAtPosition(position);

        Intent intent = new Intent(this, ActividadDetalle.class);
        intent.putExtra(ActividadDetalle.EXTRA_PARAM_ID, item.getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            ActivityOptionsCompat activityOptions =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this,
                            new Pair<View, String>(view.findViewById(R.id.imagen_coche),
                                    ActividadDetalle.VIEW_NAME_HEADER_IMAGE)
                    );

            ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
        } else
            startActivity(intent);
    }

    /*
   La clase GetCommentsTask representa una tarea asincrona que realizará
   las operaciones de red necesarias en segundo plano para obtener toda la
   lista de comentarios alojada en el servidor.
    */
    public class GetCommentsTask extends AsyncTask<URL, Void, List<String>> {

        @Override
        protected List<String> doInBackground(URL... urls) {

            List<String> comments = null;

            try {

                // Establecer la conexión
                con = (HttpURLConnection)urls[0].openConnection();

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if(statusCode!=200) {
                    comments = new ArrayList<>();
                    comments.add("El recurso no está disponible");
                    System.out.println("estatus 200");
                    return comments;
                }
                else{

                    System.out.println("disponible");

                    /*
                    Parsear el flujo con formato JSON a una lista de Strings
                    que permitan crean un adaptador
                     */
                    InputStream in = new BufferedInputStream(con.getInputStream());

                    JSONCommentsParser parser = new JSONCommentsParser();

                    comments = parser.readJsonStream(in);

                    System.out.println("salio del parser");
                    System.out.println("stream "+parser.readJsonStream(in));


                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            return comments;

        }

        @Override
        protected void onPostExecute(List<String> s) {

            /*
            Se crea un adaptador con el el resultado del parsing
            que se realizó al arreglo JSON
             */
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getBaseContext(),
                    android.R.layout.simple_list_item_1,
                    s);

            // Relacionar adaptador a la lista

//            comments.setAdapter(adapter);
        }
    }

}
