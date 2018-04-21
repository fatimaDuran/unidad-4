package com.own.sqlite1.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

/**
 * Created by devtacho on 9/03/18.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "order.db";
    private static final int CURRENT_VERSION = 1;
    private final Context context;

    interface Tables{
        String ORDER_HEADER = "order_header";
        String ORDER_DETAIL = "order_detail";
        String PRODUCT = "product";
        String CUSTOMER = "customer";
        String METHOD_PAYMENT = "method_payment";
     }

    interface References{
        String ID_ORDER_HEADER =
                String.format("REFERENCES %s(%s)" +
                                " ON DELETE CASCADE",
                        Tables.ORDER_HEADER,
                        Contract.OrderHeaders.ID);
        String ID_PRODUCT =
                String.format("REFERENCES %s(%s)" +
                                " ON DELETE CASCADE",
                        Tables.PRODUCT,
                        Contract.Products.ID);
        String ID_CUSTOMER =
                String.format("REFERENCES %s(%s)" +
                                " ON DELETE CASCADE",
                        Tables.CUSTOMER,
                        Contract.Customers.ID);
        String ID_METHOD_PAYMENT =
                String.format("REFERENCES %s(%s)" +
                                " ON DELETE CASCADE",
                        Tables.METHOD_PAYMENT,
                        Contract.MethodsPayment.ID);

    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, CURRENT_VERSION);
        this.context = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                db.setForeignKeyConstraintsEnabled(true);
            }else{
                db.execSQL("PRAGMA foreign_keys=1");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format(
                "CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT UNIQUE NOT NULL, %s DATETIME NOT NULL," +
                        "%s TEXT NOT NULL %s, %s TEXT NOT NULL %s)",
                Tables.ORDER_HEADER, BaseColumns._ID,
                Contract.OrderHeaders.ID,
                Contract.OrderHeaders.DATE,
                Contract.OrderHeaders.ID_CUSTOMER,
                References.ID_CUSTOMER,
                Contract.OrderHeaders.ID_METHOD_PAYMENT,
                References.ID_METHOD_PAYMENT));

        db.execSQL(String.format(
                "CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT NOT NULL %s," +
                        " %s INTEGER NOT NULL CHECK (%s>0)," +
                        "%s INTEGER NOT NULL %s, %s INTEGER NOT NULL, " +
                        "%s REAL NOT NULL, UNIQUE(%s, %s))",

                Tables.ORDER_DETAIL,
                BaseColumns._ID,
                Contract.OrderDetails.ID,
                References.ID_ORDER_HEADER,
                Contract.OrderDetails.SEQUENCE,
                Contract.OrderDetails.SEQUENCE,
                Contract.OrderDetails.ID_PRODUCT,
                References.ID_PRODUCT,
                Contract.OrderDetails.QUANTITY,
                Contract.OrderDetails.PRICE,
                Contract.OrderDetails.ID,
                Contract.OrderDetails.SEQUENCE));



        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY" +
                        " AUTOINCREMENT, %s TEXT NOT NULL UNIQUE, %s TEXT NOT NULL," +
                        " %s REAL NOT NULL, %s INTEGER NOT NULL CHECK(%s>=0))",
                Tables.PRODUCT, BaseColumns._ID,
                Contract.Products.ID,
                Contract.Products.NAME,
                Contract.Products.PRICE,
                Contract.Products.STOCK,
                Contract.Products.STOCK));



        db.execSQL(String.format("CREATE TABLE %s( %s INTEGER PRIMARY KEY" +
                        " AUTOINCREMENT, %s TEXT NOT NULL UNIQUE, %s TEXT" +
                        " NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL)",
                Tables.CUSTOMER,
                BaseColumns._ID,
                Contract.Customers.ID,
                Contract.Customers.FIRSTNAME,
                Contract.Customers.LASTNAME,
                Contract.Customers.PHONE));

        db.execSQL(String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY" +
                        " AUTOINCREMENT, %s TEXT NOT NULL UNIQUE, %s TEXT NOT NULL)",
                Tables.METHOD_PAYMENT,
                BaseColumns._ID,
                Contract.MethodsPayment.ID,
                Contract.MethodsPayment.NAME));


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Tables.ORDER_HEADER);
        db.execSQL("DROP TABLE IF EXISTS "+Tables.ORDER_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS "+Tables.PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS "+Tables.METHOD_PAYMENT);
        db.execSQL("DROP TABLE IF EXISTS "+Tables.CUSTOMER);
        onCreate(db);

    }
}
