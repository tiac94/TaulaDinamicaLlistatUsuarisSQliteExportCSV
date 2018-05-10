package org.ieselcaminas.alu53787365w.tauladinamicallistatusuarissqlite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by santi on 9/05/18.
 */

public class MenuEmpleatActivity extends AppCompatActivity {
    private Button btnDades;
    private Button btnBorrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuempleat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);


        //Arrepleguem el nom i cognoms de l'altra activitat
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final int IDExtra = extras.getInt("ID");
        //Cridem a la funcio per obtindre el nom del empleat, li pasem el ID com a parametre


        toolbar.setTitle("Menu de " + consultaNom(IDExtra));
        setSupportActionBar(toolbar);

        btnDades = (Button) findViewById(R.id.btnDades);
        btnBorrar = (Button) findViewById(R.id.btnBorrar);

        btnDades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Anem a l'activitat MenuEmpleatsActivity
                Intent intent = new Intent(MenuEmpleatActivity.this, DadesEmpleatActivity.class);
                //Pasem a l'altra activitat l'identificador del membre
                intent.putExtra("ID", IDExtra);
                startActivity(intent);
            }
        });
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                esborraEmpleat(IDExtra);
            }
        });
    }
    //Funcio que li pasem el ID del empleat i ens torna el nom
    private String consultaNom(int ID){
        String nom = "";
        //Connectem a la BBDD en mode lectura
        SQLiteGestor bdg = new SQLiteGestor(this,"Empleats.sqlite",null,1);
        SQLiteDatabase bd = bdg.getReadableDatabase();
        //Fem la consulta del empleat
        Cursor rs = bd.rawQuery("SELECT nom FROM EMPLEAT WHERE ID = " + ID,null);

        if (rs.moveToNext()){
            //Desem la variable el nom del empleat
            nom = rs.getString(0);
        }
        rs.close(); //Tanquem la consulta
        bd.close(); //Tanquem la BBDD
        bdg.close(); //Tanquem el gestor
        return nom;
    }
    private void esborraEmpleat(int ID){
        //Connectem al gestor de la BBDD en mode escriptura
        SQLiteGestor bdg = new SQLiteGestor(getApplicationContext(),"Empleats.sqlite",null,1);
        SQLiteDatabase bd = bdg.getWritableDatabase();
        bd.execSQL("DELETE FROM EMPLEAT WHERE ID = " + ID);
        bd.close();
        bdg.close();
        final ProgressDialog dialog = ProgressDialog.show(MenuEmpleatActivity.this, "S'està esborrant l'usuari", "Espera si us plau...", true);
        new Thread(new Runnable() {
            public void run() {
                try {
                    //---simulate doing something lengthy---
                    Thread.sleep(5000);
                    //---dismiss the dialog---
                    dialog.dismiss();

                    MenuEmpleatActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(MenuEmpleatActivity.this, "L'usuari s'ha esborrat amb èxit!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
