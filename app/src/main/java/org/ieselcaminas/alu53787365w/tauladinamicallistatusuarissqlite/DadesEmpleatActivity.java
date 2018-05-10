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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by santi on 9/05/18.
 */

public class DadesEmpleatActivity extends AppCompatActivity {
    private String nom;
    private int depart, edat;
    private double sou;
    private EditText txfDepart, txfEdat, txfSou;
    private Button btnModificar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dadesempleat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);

        txfDepart = (EditText) findViewById(R.id.txfDepart);
        txfEdat = (EditText) findViewById(R.id.txfEdat);
        txfSou = (EditText) findViewById(R.id.txfSou);
        btnModificar = (Button) findViewById(R.id.btnModificar);

        //Arrepleguem el ID del empleat de l'activitat anterior
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final int IDExtra = extras.getInt("ID");
        //Cridem a la funcio per obtindre les dades del empleat, li pasem el ID com a parametre

        consultaDadesEmpleat(IDExtra);
        toolbar.setTitle("Dades de " + nom);
        setSupportActionBar(toolbar);

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificaDadesEmpleat(IDExtra);
                Toast.makeText(getApplicationContext(), "Les dades s'han modificat amb exit!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Funcio per a obtindre les dades del empleat passant-li el ID
    private void consultaDadesEmpleat(int ID){

        //Connectem a la BBDD en mode lectura
        SQLiteGestor bdg = new SQLiteGestor(this,"Empleats.sqlite",null,1);
        SQLiteDatabase bd = bdg.getReadableDatabase();
        //Fem la consulta dels empleats
        Cursor rs = bd.rawQuery("SELECT * FROM EMPLEAT WHERE ID = " + ID,null);

        if (rs.moveToNext()){
            //Desem en variables les dades de cada empleat
            ID = rs.getInt(0);
            nom = rs.getString(1);
            depart = rs.getInt(2);
            edat = rs.getInt(3);
            sou = rs.getDouble(4);

            //Mostrem les dades en els camps de text
            txfDepart.setText(""+depart);
            txfEdat.setText(""+edat);
            txfSou.setText(""+sou);
        }
        rs.close(); //Tanquem la consulta
        bd.close(); //Tanquem la BBDD
        bdg.close(); //Tanquem el gestor
    }
    //Funcio per modificar les dades del empleat
    private void modificaDadesEmpleat(int ID){
        //Obtenim les cadenes dels camps de text
        //String nomStr = txfNom.getText().toString();
        String departStr = txfDepart.getText().toString();
        String edatStr = txfEdat.getText().toString();
        String souStr = txfSou.getText().toString();
        //Connectem a la BBDD en mode escriptura
        SQLiteGestor bdg = new SQLiteGestor(this,"Empleats.sqlite",null,1);
        SQLiteDatabase bd = bdg.getWritableDatabase();
        //Actualitzem les dades dels empleats
        bd.execSQL("UPDATE EMPLEAT SET depart = " + departStr +
                ", edat = " + edatStr + ", sou = " + souStr +  " WHERE ID = " + ID);


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