package com.own.sqlite1;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.own.sqlite1.adapter.CustomerAdapter;
import com.own.sqlite1.model.Customer;
import com.own.sqlite1.sqlite.DBOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devtacho on 15/03/18.
 */

public class CustomerActivity extends AppCompatActivity {

    private EditText edtFirstname;
    private EditText edtLastname;
    private EditText edtPhone;
    private Button btnSave;
    private DBOperations operations;
    private Customer customer = new Customer();
    private RecyclerView rcvCustomers;
    private List<Customer> customers =new ArrayList<Customer>();
    private CustomerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        //iniciacion de la instancia
        operations=DBOperations.getDBOperations(getApplicationContext());
        customer.setIdCustomer("");


        initComponents();
    }
    private void initComponents(){
        rcvCustomers = findViewById(R.id.rcv_customers);
        rcvCustomers.setHasFixedSize(true);
        LinearLayoutManager layoutManeger=new LinearLayoutManager(this);
        rcvCustomers.setLayoutManager(layoutManeger);
        //
        getCustomers();
        adapter=new CustomerAdapter(customers);

        adapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_delete:
                        operations.deleteCustomer(customers.get(rcvCustomers.getChildPosition((View)v.getParent().getParent())).getIdCustomer());
                        getCustomers();
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.btn_edit:

                        Toast.makeText(getApplicationContext(),"Editar",Toast.LENGTH_SHORT).show();
                        Cursor c = operations.getCustomerById(customers.get(
                                rcvCustomers.getChildPosition(
                                        (View)v.getParent().getParent())).getIdCustomer());
                        if (c!=null){
                            if (c.moveToFirst()){
                                customer = new Customer(c.getString(1),
                                        c.getString(2), c.getString(3), c.getString(4));
                            }
                            edtFirstname.setText(customer.getFirstname());
                            edtLastname.setText(customer.getLastname());
                            edtPhone.setText(customer.getPhone());
                        }else{
                            Toast.makeText(getApplicationContext(),"Record not found",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

            }
        });
        rcvCustomers.setAdapter(adapter);

        edtFirstname =(EditText)findViewById(R.id.edt_firstname);
        edtLastname =(EditText)findViewById(R.id.edt_lastname);
        edtPhone =(EditText)findViewById(R.id.edt_phone);

        btnSave =(Button)findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!customer.getIdCustomer().equals("")){
                    customer.setFirstname(edtFirstname.getText().toString());
                    customer.setLastname(edtLastname.getText().toString());
                    customer.setPhone(edtPhone.getText().toString());
                    operations.updateCustomer(customer);

                }else {
                    customer = new Customer("", edtFirstname.getText().toString(), edtLastname.getText().toString(), edtPhone.getText().toString());
                    operations.insertCustomer(customer);
                }
                getCustomers();
                clearData();
                adapter.notifyDataSetChanged();
            }
        });

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
        edtFirstname.setText("");
        edtLastname.setText("");
        edtPhone.setText("");
        customer =null;
        customer = new Customer();
        customer.setIdCustomer("");
    }

}
