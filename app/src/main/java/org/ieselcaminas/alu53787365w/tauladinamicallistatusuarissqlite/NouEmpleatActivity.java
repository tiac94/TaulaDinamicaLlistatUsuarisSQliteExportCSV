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

/**
 * Created by santi on 4/05/18.
 */
public class NouEmpleatActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouempleat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        toolbar.setTitle("Crear empleat");
        setSupportActionBar(toolbar);

        final EditText txfNom, txfDepart, txfEdat, txfSou;
        Button btnCreaEmpleat;

        txfNom = (EditText) findViewById(R.id.txfNom);
        txfDepart = (EditText) findViewById(R.id.txfDepart);
        txfEdat = (EditText) findViewById(R.id.txfEdat);
        txfSou = (EditText) findViewById(R.id.txfSou);
        btnCreaEmpleat = (Button) findViewById(R.id.btnCreaEmpleat);

        btnCreaEmpleat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Traem les cadenes dels camps de text
                String nomStr = txfNom.getText().toString();
                String departStr = txfDepart.getText().toString();
                String edatStr = txfEdat.getText().toString();
                String souStr = txfSou.getText().toString();

                //Cadena per mostrar l'error en la validacio de dades
                String cad = "";
                //Vble per validar les dades (nom i depart camps obligatoris)
                boolean semaf = true;
                //Si el camp nom esta buit i la vble es vartadera
                if ((nomStr.isEmpty()) && (semaf)){
                    semaf = false;
                    txfNom.requestFocus();  //Posem el focus al camp nom
                    cad = "ERROR! Camp nom obligatori.";
                    Toast.makeText(getApplicationContext(), cad, Toast.LENGTH_SHORT).show();
                }

                //Si el camp depart esta buit i la vble es vertadera
                if ((departStr.isEmpty()) && (semaf)){
                    semaf = false;
                    txfDepart.requestFocus();  //Posem el focus al camp depart
                    cad = "ERROR! Camp Departament obligatori.";
                    Toast.makeText(getApplicationContext(), cad, Toast.LENGTH_SHORT).show();
                }

                //Ara comprovem que no es repetisquen els noms
                //Connectem al gestor de BBDD en mode lectura
                SQLiteGestor bdg = new SQLiteGestor(getApplicationContext(),"Empleats.sqlite",null,1);
                SQLiteDatabase bdr = bdg.getReadableDatabase();
                //Fem la consulta
                Cursor rs = bdr.rawQuery("SELECT nom FROM EMPLEAT",null);
                //Si la vble es vertadera
                if (semaf){
                    //Recorrem la consulta
                    while (rs.moveToNext()) {
                        //Guardem en una vble el nom
                        String nomRS = rs.getString(0);
                        //Si el nom del camp de text s'encontra en la BBDD
                        if (nomRS.equals(nomStr)) {
                            cad = "ERROR! Aquest nom ja esta en la base de dades";
                            Toast.makeText(getApplicationContext(), cad, Toast.LENGTH_SHORT).show();
                            //Missatge d'error
                            semaf = false;  //assignem vble falsa
                            txfNom.requestFocus();     //posem el focus al camp nom
                        }
                    }
                }

                rs.close();     //Tanquem la consulta
                bdr.close();  //Tanquem la BBDD
                bdg.close();  //Tanquem el gestor

                //Si tot esta correcte inserirem les dades
                if (semaf){
                    //Connectem a la BBDD en mode lectura
                    SQLiteGestor bdg2 = new SQLiteGestor(getApplicationContext(),"Empleats.sqlite",null,1);
                    SQLiteDatabase bdr2 = bdg2.getReadableDatabase();
                    //Fem la consulta per traure el nº de membres
                    Cursor rs2 = bdr2.rawQuery("SELECT MAX(ID) FROM EMPLEAT",null);
                    if(rs2.moveToNext()){
                        //vble per desar el nº de membres
                        int id = rs2.getInt(0);
                        //Connectem a la BBDD en mode escriptura
                        SQLiteGestor bdg3 = new SQLiteGestor(getApplicationContext(),"Empleats.sqlite",null,1);
                        SQLiteDatabase bd3 = bdg3.getWritableDatabase();
                        //Inserim un nou membre. El id sera el (nº membres+1)
                        bd3.execSQL("INSERT INTO EMPLEAT VALUES (" + (id+1) + ", '" + nomStr + ", "
                                + departStr + ", " + edatStr + ", " + souStr + ")");
                        cad = "El contacte s'ha creat amb èxit!";
                        //Missatge d'exit
                        Toast.makeText(NouEmpleatActivity.this, cad, Toast.LENGTH_SHORT).show();

                        bd3.close();  //Tanquem la BBDD
                        bdg3.close(); //Tanquem el gestor
                    }
                    rs2.close();     //Tanquem la consulta
                    bdr2.close();  //Tanquem la BBDD
                    bdg2.close();  //Tanquem el gestor

                }

            }
        });

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
