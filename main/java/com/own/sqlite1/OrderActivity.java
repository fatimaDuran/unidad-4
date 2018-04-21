package com.own.sqlite1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.own.sqlite1.adapter.OrderAdapter;
import com.own.sqlite1.model.Customer;
import com.own.sqlite1.model.MethodPayment;
import com.own.sqlite1.model.OrderHeader;
import com.own.sqlite1.sqlite.Contract;
import com.own.sqlite1.sqlite.DBOperations;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class OrderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private Spinner spiCustomer;
    private Spinner spiMethod;
    private EditText edtDate;
    private ImageButton btnDate;
    private Button btnSave;
    private DBOperations operations;
    private OrderHeader order = new OrderHeader();
    private RecyclerView rcvOrder;
    private List<OrderHeader> orders =new ArrayList<OrderHeader>();
    private List<Customer> customers =new ArrayList<Customer>();
    private List<MethodPayment> methods =new ArrayList<MethodPayment>();
    private OrderAdapter orderAdapter;
    private SimpleCursorAdapter customerAdapter;
    private SimpleCursorAdapter methodAdapter;
    private Customer customer;
    private MethodPayment method;
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        operations=DBOperations.getDBOperations(getApplicationContext());
        order.setIdOrderHeader("");
        initComponents();
    }
    private void initComponents(){
        rcvOrder = findViewById(R.id.rcv_order);
        rcvOrder.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        rcvOrder.setLayoutManager(layoutManager);
        getOrders();
        getCustomers();
        getMethods();
        orderAdapter =new OrderAdapter(orders, operations);

        orderAdapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_detail:
                        Intent intent = new Intent(OrderActivity.this,
                                OrderDetailActivity.class);
                        Bundle bundle = new Bundle();
                        order = orders.get(rcvOrder.getChildPosition((View)v.getParent().getParent()));
                        bundle.putSerializable("order", order);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case R.id.btn_delete:
                        operations.deleteOrderHeader(orders.get(rcvOrder.getChildPosition((View)v.getParent().getParent())).getIdOrderHeader());
                        getOrders();
                        orderAdapter.notifyDataSetChanged();
                        clearData();
                        break;
                    case R.id.btn_edit:
                        Cursor c = operations.getOrderById(orders.get(
                                rcvOrder.getChildPosition((View)v.getParent().getParent())).getIdOrderHeader());
                        if (c!=null) {

                            if (c.moveToFirst()) {
                                order = new OrderHeader(c.getString(0),
                                        c.getString(1), c.getString(2), c.getString(3));
                                c = operations.getCustomerById(order.getIdCustomer());
                                c.moveToFirst();
                                for (Customer custom: customers) {
                                    if(c.getString(1).equals(custom.getIdCustomer())){
                                        customer = custom;
                                        break;
                                    }
                                }

                                c = operations.getMethodPaymentById(order.getIdMethodPayment());
                                c.moveToFirst();
                                for (MethodPayment met: methods) {
                                    if(c.getString(1).equals(met.getIdMethodPayment())){
                                        method = met;
                                        break;
                                    }
                                }

                            }

                            spiCustomer.setSelection(customers.indexOf(customer));
                            spiMethod.setSelection(methods.indexOf(method));
                            edtDate.setText(order.getDate());


                        }else{
                            Toast.makeText(getApplicationContext(),"Record not found",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

            }
        });
        rcvOrder.setAdapter(orderAdapter);

        spiCustomer =(Spinner) findViewById(R.id.spi_customer);
        spiMethod =(Spinner) findViewById(R.id.spi_method);

        methodAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item,
                operations.getMethodsPayment(),
                new String[]{Contract.MethodsPayment.NAME},
                new int[]{android.R.id.text1},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        spiMethod.setAdapter(methodAdapter);

        customerAdapter = new SimpleCursorAdapter(this,
                android.R.layout.two_line_list_item,
                operations.getCustomers(),
                new String[]{Contract.Customers.FIRSTNAME, Contract.Customers.LASTNAME},
                new int[]{android.R.id.text1, android.R.id.text2},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        spiCustomer.setAdapter(customerAdapter);

        edtDate = findViewById(R.id.edt_date);
        btnDate = findViewById(R.id.btn_date);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment fragment =
                        new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("date", edtDate.getText().toString());
                fragment.setArguments(bundle);
                fragment.show(getFragmentManager(), "date");
            }
        });

        btnSave =(Button)findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customer = customers.get(spiCustomer.getSelectedItemPosition());
                method = methods.get(spiMethod.getSelectedItemPosition());
                if (!order.getIdOrderHeader().equals("")){
                    order.setIdCustomer(customer.getIdCustomer());
                    order.setIdMethodPayment(method.getIdMethodPayment());
                    order.setDate(edtDate.getText().toString());
                    operations.updateOrderHeader(order);

                }else {
                        order = new OrderHeader("",customer.getIdCustomer(),
                        method.getIdMethodPayment(), edtDate.getText().toString());
                    operations.insertOrderHeader(order);
                }
                getOrders();
                clearData();
                orderAdapter.notifyDataSetChanged();
            }
        });

    }
    private void getOrders(){
        Cursor c =operations.getOrders();
        orders.clear();
        if(c!=null){
            while (c.moveToNext()){

                orders.add(new OrderHeader(c.getString(0),c.getString(1),c.getString(2),c.getString(3)));
            }
        }
    }
    private void getMethods(){
        Cursor c =operations.getMethodsPayment();
        methods.clear();
        if (c!=null){
            while(c.moveToNext()){
                methods.add(new MethodPayment(c.getString(1),c.getString(2)));
            }
        }
    }
    private void getCustomers(){
        Cursor c =operations.getCustomers();
        customers.clear();
        if(c!=null){
            while (c.moveToNext()){
                customers.add(new Customer(c.getString(1),c.getString(2),c.getString(3),c.getString(4)));
            }
        }
    }

    private void clearData(){
        spiCustomer.setSelection(0);
        spiMethod.setSelection(0);
        edtDate.setText("");
        order =null;
        order = new OrderHeader();
        order.setIdOrderHeader("");
    }

    private void setDate(final Calendar calendar){
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        edtDate.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onDateSet(DatePicker datePicker,
                          int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(year, month, day);
        setDate(calendar);
    }



    public static class DatePickerFragment extends DialogFragment{

        @Override
        public Dialog onCreateDialog(Bundle bundle) {
            // Use the current date as the default date in the picker
            final Calendar calendar = Calendar.getInstance();
            int year, month, day;
            bundle = getArguments();
            if(!bundle.getString("date").equals("")){
                DateFormat format = DateFormat.getDateInstance();
                try {
                    Date dateEdit = format.parse(
                            bundle.getString("date"));
                    calendar.setTime(dateEdit);
                }catch (ParseException e){
                    e.printStackTrace();
                }
            }
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);


            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener) getActivity(),
                    year, month, day);
        }

    }
}
