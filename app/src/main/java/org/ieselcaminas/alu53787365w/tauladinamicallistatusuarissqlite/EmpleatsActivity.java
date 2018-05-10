package org.ieselcaminas.alu53787365w.tauladinamicallistatusuarissqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by santi on 7/05/18.
 */

public class EmpleatsActivity extends AppCompatActivity {
    ListView lstOpciones;
    ArrayList<Usuari> empleats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        toolbar.setTitle("Empleats");
        setSupportActionBar(toolbar);
        consultaEmpleats();

        //Creem l'adaptador
        AdaptadorTitulares adaptador =
                new AdaptadorTitulares(this, empleats);

        lstOpciones = (ListView)findViewById(R.id.LstOpciones);
        lstOpciones.setAdapter(adaptador);
        //Quan premem sobre un membre anirem a l'activitat per consultar les seues dades
        lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Anem a l'activitat MenuEmpleatsActivity
                Intent intent = new Intent(EmpleatsActivity.this, MenuEmpleatActivity.class);
                //Pasem a l'altra activitat l'identificador del membre
                intent.putExtra("ID", empleats.get(i).getID());
                startActivity(intent);
                finish();
            }
        });
    }
    //Metode per crear una llista d'empleats
    private void consultaEmpleats(){
        //Cridem a la funcio consultaEmpleats();

        empleats = new ArrayList<>();

        //Connectem a la BBDD en mode lectura
        SQLiteGestor bdg = new SQLiteGestor(this,"Empleats.sqlite",null,1);
        SQLiteDatabase bd = bdg.getReadableDatabase();
        //Fem la consulta dels empleats
        Cursor rs = bd.rawQuery("SELECT * FROM EMPLEAT",null);

        while (rs.moveToNext()){
            //Desem en variables les dades de cada empleat
            int ID = rs.getInt(0);
            String nom = rs.getString(1);
            int depart = rs.getInt(2);
            int edat = rs.getInt(3);
            double sou = rs.getDouble(4);

            //Desem les dades en una llista
            empleats.add(new Usuari(ID, nom, depart, edat, sou));
        }
        rs.close(); //Tanquem la consulta
        bd.close(); //Tanquem la BBDD
        bdg.close(); //Tanquem el gestor
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
