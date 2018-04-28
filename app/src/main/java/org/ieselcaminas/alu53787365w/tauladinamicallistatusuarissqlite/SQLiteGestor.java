package org.ieselcaminas.alu53787365w.tauladinamicallistatusuarissqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteGestor extends SQLiteOpenHelper {
    public SQLiteGestor(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE EMPLEAT("
                + "num INTEGER CONSTRAINT cp_emp PRIMARY KEY, "
                + "nom TEXT, " + "depart INTEGER, " + "edat INTEGER, "
                + "sou REAL " + ")";
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL("INSERT INTO EMPLEAT VALUES (1,'Andreu',10,32,1000.0)");
        sqLiteDatabase.execSQL("INSERT INTO EMPLEAT VALUES (2,'Bernat',20,28,1200.0)");
        sqLiteDatabase.execSQL("INSERT INTO EMPLEAT VALUES (3,'Clàudia',10,26,1100.0)");
        sqLiteDatabase.execSQL("INSERT INTO EMPLEAT VALUES (4,'Damià',10,40,1500.0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}