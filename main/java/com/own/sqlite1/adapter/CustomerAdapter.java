package com.own.sqlite1.adapter;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.own.sqlite1.R;
import com.own.sqlite1.model.Customer;
import com.own.sqlite1.model.Product;

import java.util.List;


public class CustomerAdapter extends RecyclerView.Adapter <CustomerAdapter.CustomerViewHolder>
        implements View.OnClickListener {

    List<Customer> customers;
    View.OnClickListener listener;
    //Constructor
    public CustomerAdapter(List<Customer> customers){
        this.customers =customers;
    }
//getter and setter de listener
    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       //se inflael cardview al reciclerview
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_cell_layout,parent,false);
        CustomerViewHolder holder=new CustomerViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        holder.txvFirstName.setText(customers.get(position).getFirstname());
        holder.txvLastName.setText(customers.get(position).getLastname());
        holder.txvPhone.setText(customers.get(position).getPhone());
        holder.setListener(this);

    }

    @Override
    public int getItemCount() {
        return customers.size();
    }
    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }

    }

    public static class CustomerViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        CardView crvCustomer;
        TextView txvFirstName;
        TextView txvLastName;
        TextView txvPhone;
        ImageButton btnEdit;
        ImageButton btnDelete;
        View.OnClickListener listener;




        public CustomerViewHolder(View itemView) {
            super(itemView);
            crvCustomer=(CardView) itemView.findViewById(R.id.crv_customer);
            txvFirstName=(TextView)itemView.findViewById(R.id.txv_firstname);
            txvLastName=(TextView)itemView.findViewById(R.id.txv_lastname);
            txvPhone=(TextView)itemView.findViewById(R.id.txv_phone);
            btnEdit =(ImageButton) itemView.findViewById(R.id.btn_edit);
            btnDelete =(ImageButton) itemView.findViewById(R.id.btn_delete);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
}

        @Override
        public void onClick(View v) {
       if (listener!=null){
           listener.onClick(v);
       }
        }

        public void setListener(View.OnClickListener listener){
            this.listener=listener;

        }
    }

}
