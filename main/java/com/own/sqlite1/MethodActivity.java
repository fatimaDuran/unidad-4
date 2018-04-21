package com.own.sqlite1;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.own.sqlite1.adapter.MethodAdapter;
import com.own.sqlite1.model.MethodPayment;
import com.own.sqlite1.sqlite.DBOperations;

import java.util.ArrayList;
import java.util.List;

public class MethodActivity extends AppCompatActivity {

    private EditText edtName;
    private Button btnSave;
    private DBOperations operations;
    private MethodPayment method = new MethodPayment();
    private RecyclerView rcvMethod;
    private List<MethodPayment> methods =new ArrayList<MethodPayment>();
    private MethodAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_payment);

        //iniciacion de la isntancia
        operations=DBOperations.getDBOperations(getApplicationContext());
        method.setIdMethodPayment("");


        initComponents();
    }
    private void initComponents(){
        rcvMethod =(RecyclerView)findViewById(R.id.rcv_methods);
        rcvMethod.setHasFixedSize(true);
        LinearLayoutManager layoutManeger=new LinearLayoutManager(this);
        rcvMethod.setLayoutManager(layoutManeger);
        //
        getMethods();
        adapter=new MethodAdapter(methods);

        adapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_delete:
                        operations.deleteMethodPayment(methods.get(rcvMethod.getChildPosition((View)v.getParent().getParent())).getIdMethodPayment());
                        getMethods();
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.btn_edit:

                        Toast.makeText(getApplicationContext(),"Editar",Toast.LENGTH_SHORT).show();
                        Cursor c = operations.getMethodPaymentById(methods.get(
                                rcvMethod.getChildPosition(
                                        (View)v.getParent().getParent())).getIdMethodPayment());
                        if (c!=null){
                            if (c.moveToFirst()){
                                method = new MethodPayment(c.getString(1),c.getString(2));
                            }
                            edtName.setText(method.getName());
                        }else{
                            Toast.makeText(getApplicationContext(),"Registro no encontrado",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

            }
        });
        rcvMethod.setAdapter(adapter);

        edtName =(EditText)findViewById(R.id.edt_name);
        btnSave =(Button)findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!method.getIdMethodPayment().equals("")){
                    method.setName(edtName.getText().toString());
                    operations.updateMethodPayment(method);

                }else {
                    method = new MethodPayment("", edtName.getText().toString());
                    operations.insertMethodPayment(method);
                }
                getMethods();
                clearData();
                adapter.notifyDataSetChanged();
            }
        });

    }
    private void getMethods(){
        Cursor c =operations.getMethodsPayment();
        methods.clear();
        if(c!=null){
            while (c.moveToNext()){
                methods.add(new MethodPayment(c.getString(1),c.getString(2)));
            }
        }

    }

    private void clearData(){
        edtName.setText("");
        method =null;
        method = new MethodPayment();
        method.setIdMethodPayment("");
    }
}
