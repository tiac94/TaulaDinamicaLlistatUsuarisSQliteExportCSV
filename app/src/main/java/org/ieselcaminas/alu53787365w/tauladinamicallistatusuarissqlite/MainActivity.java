package org.ieselcaminas.alu53787365w.tauladinamicallistatusuarissqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList <Usuari> usuaris;
    Button exportar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tabla tabla = new Tabla(this, (TableLayout) findViewById(R.id.tabla));
        //Agreguem les capçaleres
        tabla.agregarCabecera(R.array.cabecera_tabla);
        consultaUsuaris();
        //Recorrem el llistat d'usuaris
        for (int i = 0; i < usuaris.size(); i++) {
            //Declarem una llista per guardar en una fila la informacio de cada usuari
            ArrayList<String> elementos = new ArrayList<String>();

            String nom, departStr, edatStr, souStr;
            int depart, edat;
            double sou;

            nom = usuaris.get(i).getNom();
            depart = usuaris.get(i).getDepart();
            edat = usuaris.get(i).getEdat();
            sou = usuaris.get(i).getSou();

            //Passem els enters i decimals a cadenes
            departStr = String.valueOf(depart);
            edatStr = String.valueOf(edat);
            souStr = String.valueOf(sou);
            elementos.add(nom);
            elementos.add(departStr);
            elementos.add(edatStr);
            elementos.add(souStr);

            //cridem a aquesta funcio per afegir la llista en una fila de la taula
            tabla.agregarFilaTabla(elementos);
        }
        exportar = (Button) findViewById(R.id.export);
        exportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportarCSV();
            }
        });


    }

    //Funcio per consultar els usuaris en la BBDD
    private void consultaUsuaris() {
        //Declarem la llista d'objectes de la clase Usuari
        usuaris = new ArrayList<Usuari>();
        //Connectem a la BBDD en mode lectura
        SQLiteGestor bdg = new SQLiteGestor(this,"Empleats.sqlite",null,1);
        SQLiteDatabase bd = bdg.getReadableDatabase();
        //Fem la consulta
        Cursor rs = bd.rawQuery("SELECT * FROM EMPLEAT",null);
        //Recorrem els resultats de la consulta i els acumulem en el llistat d'usuaris
        while (rs.moveToNext()){
            usuaris.add(new Usuari(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4)));
        }
        //Tanquem la consulta i la connexio
        rs.close();
        bd.close();
        bdg.close();
    }
    //Funció per exportar les dades a una fulla de càlcul
    private void exportarCSV(){
        //Declarem un objecte de la clase Calendar per traure l'any i mes actual
        Calendar c1 = Calendar.getInstance();
        String any = String.valueOf(c1.get(Calendar.YEAR));
        String mes = String.valueOf(c1.get(Calendar.MONTH)+1);
        //Nom del fitxer de la fulla de càlcul
        String csv = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/empleats" + any + mes + ".csv";

        //Creem un objecte de la clase CSVWriter
        CSVWriter writer = null;
        try {
            //Declarem l'objecte de la clase CSVWriter
            writer = new CSVWriter(new FileWriter(csv));
            //Declarem una llista de llistes de cadenes
            List<String[]> data = new ArrayList<String[]>();
            //Recorrem el llistat d'usuaris
            for (int i = 0; i < usuaris.size(); i++) {
                String nom, departStr, edatStr, souStr;
                int depart, edat;
                double sou;

                nom = usuaris.get(i).getNom();
                depart = usuaris.get(i).getDepart();
                edat = usuaris.get(i).getEdat();
                sou = usuaris.get(i).getSou();

                //Passem els enters i decimals a cadenes
                departStr = String.valueOf(depart);
                edatStr = String.valueOf(edat);
                souStr = String.valueOf(sou);
                //Afegim la informacio de l'usuari en una fila
                data.add(new String[]{nom, departStr, edatStr, souStr});
            }
            //Amb aquest mètode assignem la llista a l'objecte de la classe CSVWriter
            writer.writeAll(data);
            //Tanquem l'objecte
            writer.close();
            //Missatges
            String cad = "Descarregant...";
            String cad2 = "Les dades s'han exportat amb èxit!";
            String cad3 = "El fitxer s'ha desat en " + csv;
            Toast.makeText(getApplicationContext(), cad, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), cad2, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), cad3, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
