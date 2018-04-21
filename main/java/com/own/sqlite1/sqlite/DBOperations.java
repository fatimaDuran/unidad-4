package com.own.sqlite1.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.own.sqlite1.model.Customer;
import com.own.sqlite1.model.MethodPayment;
import com.own.sqlite1.model.OrderDetail;
import com.own.sqlite1.model.OrderHeader;
import com.own.sqlite1.model.Product;


/**
 * Created by Marili Arevalo on 23/02/2017.
 */

public final class DBOperations {
    private static DBHelper db;
    private static DBOperations operations;

    private static final String JOIN_ORDER_CUSTOMER_METHOD =
            "order_header " +
            "INNER JOIN customer " +
            "ON order_header.id_customer = customer.id " +
            "INNER JOIN method_payment " +
            "ON order_header.id_method_payment = method_payment.id";
    private final String[] orderProj = new String[]{
            DBHelper.Tables.ORDER_HEADER + "." +
                    Contract.OrderHeaders.ID,
                    Contract.OrderHeaders.ID_CUSTOMER,
                    Contract.OrderHeaders.ID_METHOD_PAYMENT,
                    Contract.OrderHeaders.DATE,
                    Contract.Customers.FIRSTNAME,
                    Contract.Customers.LASTNAME,
                    Contract.MethodsPayment.NAME
            };

    private DBOperations(){

    }

    public static DBOperations getDBOperations(
            Context context){
        if(operations==null) {
            operations = new DBOperations();
        }
        if(db==null){
            db = new DBHelper(context);
        }
        return operations;
    }
    //Operations of  Orders
    public Cursor getOrders(){
        SQLiteDatabase database = db.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(JOIN_ORDER_CUSTOMER_METHOD);

        return  builder.query(database, orderProj,
                null, null, null, null, null);
    }

    public Cursor getOrderById(String id){
        SQLiteDatabase database = db.getWritableDatabase();
        String selection = String.format("%s=?",
                DBHelper.Tables.ORDER_HEADER+"."+Contract.OrderHeaders.ID);
        String[] selectionArgs = {id};
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(JOIN_ORDER_CUSTOMER_METHOD);
        String[] projection = {
                DBHelper.Tables.ORDER_HEADER+"."
                        + Contract.OrderHeaders.ID,
                Contract.OrderHeaders.ID_CUSTOMER,
                Contract.OrderHeaders.ID_METHOD_PAYMENT,
                Contract.OrderHeaders.DATE,
                Contract.Customers.FIRSTNAME,
                Contract.Customers.LASTNAME,
                Contract.MethodsPayment.NAME
        };
        return builder.query(database, projection, selection,
                selectionArgs, null, null, null);
    }

    public String insertOrderHeader(OrderHeader orderHeader){
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        String idOrderHeader =
                Contract.OrderHeaders.generateIdOrderHeader();
        values.put(Contract.OrderHeaders.ID, idOrderHeader);
        values.put(Contract.OrderHeaders.ID_CUSTOMER,
                orderHeader.getIdCustomer());
        values.put(Contract.OrderHeaders.ID_METHOD_PAYMENT,
                orderHeader.getIdMethodPayment());
        values.put(Contract.OrderHeaders.DATE,
                orderHeader.getDate());

        database.insertOrThrow(DBHelper.Tables.ORDER_HEADER,
                null, values);
        return idOrderHeader;
    }

    public boolean updateOrderHeader(OrderHeader orderHeader){
        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contract.OrderHeaders.DATE,
                orderHeader.getDate());
        values.put(Contract.OrderHeaders.ID_CUSTOMER,
                orderHeader.getIdCustomer());
        values.put(Contract.OrderHeaders.ID_METHOD_PAYMENT,
                orderHeader.getIdMethodPayment());

        String whereClause = String.format("%s=?", Contract.OrderHeaders.ID);
        String[] whereArgs = {orderHeader.getIdOrderHeader()};

        int result = database.update(DBHelper.Tables.ORDER_HEADER, values, whereClause, whereArgs);
        return result>0;
    }

    public boolean deleteOrderHeader(String idOrderHeader){
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause =
                Contract.OrderHeaders.ID + "=?";
        String[] whereArgs = {idOrderHeader};

        int result =  database.delete(
                DBHelper.Tables.ORDER_HEADER, whereClause, whereArgs);
        return result > 0;
    }
    //Operations of  OrderDetails
    public Cursor getOrderDetails(){
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s",
                DBHelper.Tables.ORDER_DETAIL);
        return  database.rawQuery(sql, null);
    }

    public Cursor getOrderDetailById(String id){
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s" +
                " WHERE %s=?", DBHelper.Tables.ORDER_DETAIL,
                Contract.OrderHeaders.ID);
        String[] whereArgs = {id};
        return database.rawQuery(sql, whereArgs);
    }

    public String insertOrderDetail(OrderDetail orderDetail){
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.OrderDetails.ID,
                orderDetail.getIdOrderHeader());
        values.put(Contract.OrderDetails.SEQUENCE,
                orderDetail.getSequence());
        values.put(Contract.OrderDetails.ID_PRODUCT,
                orderDetail.getIdProduct());
        values.put(Contract.OrderDetails.QUANTITY,
                orderDetail.getQuantity());
        values.put(Contract.OrderDetails.PRICE,
                orderDetail.getPrice());

        database.insertOrThrow(DBHelper.Tables.ORDER_DETAIL,
                null, values);
        return String.format("%s#%d",
                orderDetail.getIdOrderHeader(),
                orderDetail.getSequence());
    }

    public boolean updateOrderDetail(OrderDetail orderDetail){
        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contract.OrderDetails.SEQUENCE,
                orderDetail.getSequence());
        values.put(Contract.OrderDetails.QUANTITY,
                orderDetail.getQuantity());
        values.put(Contract.OrderDetails.PRICE,
                orderDetail.getPrice());

        String whereClause = String.format("%s=? AND %s=?",
                Contract.OrderDetails.ID,
                Contract.OrderDetails.SEQUENCE);
        String[] whereArgs = {orderDetail.getIdOrderHeader(),
        String.valueOf(orderDetail.getSequence())};

        int result = database.update(DBHelper.Tables.ORDER_DETAIL,
                values, whereClause, whereArgs);
        return result>0;
    }

    public boolean deleteOrderDetail(String idOrderDetail, String sequence){
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause =
                Contract.OrderDetails.ID + "=? AND "+
                Contract.OrderDetails.SEQUENCE + "=?";
        String[] whereArgs = {idOrderDetail, sequence};

        int result =  database.delete(
                DBHelper.Tables.ORDER_DETAIL, whereClause, whereArgs);
        return result > 0;
    }

    public boolean deleteOrderDetail(String idOrderDetail){
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause =
                Contract.OrderDetails.ID + "=?";
        String[] whereArgs = {idOrderDetail};

        int result =  database.delete(
                DBHelper.Tables.ORDER_DETAIL, whereClause, whereArgs);
        return result > 0;
    }


    //Operations of  Product
    public Cursor getProducts(){
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s",
                DBHelper.Tables.PRODUCT);
        return  database.rawQuery(sql, null);
    }

    public Cursor getProductById(String id){
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s" +
                        " WHERE %s=?",
                DBHelper.Tables.PRODUCT,
                Contract.Products.ID);
        String[] whereArgs = {id};
        return database.rawQuery(sql, whereArgs);
    }

    public String insertProduct(Product product){
        SQLiteDatabase database = db.getWritableDatabase();
        String idProduct = Contract.Products.generateIdProduct();
        ContentValues values = new ContentValues();
        values.put(Contract.Products.ID, idProduct);
        values.put(Contract.Products.NAME, product.getName());
        values.put(Contract.Products.PRICE, product.getPrice());
        values.put(Contract.Products.STOCK, product.getStock());
        database.insertOrThrow(DBHelper.Tables.PRODUCT, null, values);
        return idProduct;
    }

    public boolean updateProduct(Product product){
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.Products.NAME, product.getName());
        values.put(Contract.Products.PRICE, product.getPrice());
        values.put(Contract.Products.STOCK, product.getStock());
        String whereClause = String.format("%s=?", Contract.Products.ID);
        String[] whereArgs = {product.getIdProduct()};
        int result = database.update(DBHelper.Tables.PRODUCT,
                values, whereClause, whereArgs);
        return result>0;
    }

    public boolean deleteProduct(String idProduct) {
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause = Contract.Products.ID + "=?";
        String[] whereArgs = {idProduct};
        int result = database.delete(DBHelper.Tables.PRODUCT,
                whereClause, whereArgs);
        return result > 0;
    }



    // Operations Customers
    public Cursor getCustomers() {
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s",
                DBHelper.Tables.CUSTOMER);
        return database.rawQuery(sql, null);
    }
    public Cursor getCustomerById(String idCustomer) {
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                DBHelper.Tables.CUSTOMER,
                Contract.Customers.ID);
        String[] whereArgs ={idCustomer};
        return database.rawQuery(sql, whereArgs);
    }

    public String insertCustomer(Customer customer) {
        SQLiteDatabase database = db.getWritableDatabase();
        String idCustomer = Contract.Customers.generateIdCustomer();
        ContentValues values = new ContentValues();
        values.put(Contract.Customers.ID, idCustomer);
        values.put(Contract.Customers.FIRSTNAME, customer.getFirstname());
        values.put(Contract.Customers.LASTNAME, customer.getLastname());
        values.put(Contract.Customers.PHONE, customer.getPhone());

        return database.insertOrThrow(DBHelper.Tables.CUSTOMER,
                null, values) > 0 ? idCustomer : null;
    }

    public boolean updateCustomer(Customer customer) {
        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contract.Customers.FIRSTNAME,
                customer.getFirstname());
        values.put(Contract.Customers.LASTNAME,
                customer.getLastname());
        values.put(Contract.Customers.PHONE,
                customer.getPhone());
        String whereClause = String.format("%s=?",
                Contract.Customers.ID);
        final String[] whereArgs = {customer.getIdCustomer()};
        int result = database.update(DBHelper.Tables.CUSTOMER,
                values, whereClause, whereArgs);
        return result > 0;
    }
    public boolean deleteCustomer(String idCustomer) {
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause = String.format("%s=?",
                Contract.Customers.ID);
        final String[] whereArgs = {idCustomer};
        int result = database.delete(DBHelper.Tables.CUSTOMER, whereClause, whereArgs);
        return result > 0;
    }






    // Operation Method of payment
    public Cursor getMethodsPayment() {
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s",
                DBHelper.Tables.METHOD_PAYMENT);
        return database.rawQuery(sql, null);
    }

    public Cursor getMethodPaymentById(String idMethodPayment) {
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                DBHelper.Tables.METHOD_PAYMENT,
                Contract.MethodsPayment.ID);
        String[] whereArgs = {idMethodPayment};
        return database.rawQuery(sql, whereArgs);
    }
    public String insertMethodPayment(MethodPayment methodPayment) {
        SQLiteDatabase database = db.getWritableDatabase();

        String idMethodPayment = Contract.MethodsPayment.generateIdMethodPayment();

        ContentValues values = new ContentValues();
        values.put(Contract.MethodsPayment.ID, idMethodPayment);
        values.put(Contract.MethodsPayment.NAME, methodPayment.getName());

        return database.insertOrThrow(
                DBHelper.Tables.METHOD_PAYMENT, null,
                values) > 0 ? idMethodPayment : null;
    }

    public boolean updateMethodPayment(MethodPayment methodPayment) {
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.MethodsPayment.NAME,
                methodPayment.getName());
        String whereClause = String.format("%s=?",
                Contract.MethodsPayment.ID);
        String[] whereArgs = {methodPayment.getIdMethodPayment()};
        int result = database.update(
                DBHelper.Tables.METHOD_PAYMENT, values,
                whereClause, whereArgs);
        return result > 0;
    }

    public boolean deleteMethodPayment(String idMethodPayment) {
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause = String.format("%s=?", Contract.MethodsPayment.ID);
        String[] whereArgs = {idMethodPayment};
        int result = database.delete(DBHelper.Tables.METHOD_PAYMENT, whereClause, whereArgs);
        return result > 0;
    }


    public SQLiteDatabase getDb() {
        return db.getWritableDatabase();
    }


}
