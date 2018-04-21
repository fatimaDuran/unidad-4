package com.own.sqlite1.adapter;


import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.own.sqlite1.R;
import com.own.sqlite1.model.Customer;
import com.own.sqlite1.model.MethodPayment;
import com.own.sqlite1.model.OrderHeader;
import com.own.sqlite1.sqlite.DBOperations;

import java.util.List;


public class OrderAdapter extends RecyclerView.Adapter <OrderAdapter.OrderViewHolder>
        implements View.OnClickListener {

    List<OrderHeader> orders;
    Customer customer;
    MethodPayment method;
    DBOperations operations;
    View.OnClickListener listener;
    //Constructor
    public OrderAdapter(List<OrderHeader> orders, DBOperations operations){
        this.orders = orders;
        this.operations = operations;

    }
//getter and setter de listener
    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       //se infla el cardview al reciclerview
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_cell_layout,parent,false);
        OrderViewHolder holder=new OrderViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Cursor c = operations.getCustomerById(orders.get(position).getIdCustomer());
        if(c.moveToNext()){
            customer = new Customer(c.getString(1), c.getString(2), c.getString(3),
                    c.getString(4));
        }
        c=operations.getMethodPaymentById(orders.get(position).getIdMethodPayment());
        if(c.moveToNext()){
            method = new MethodPayment(c.getString(1), c.getString(2));
        }
        holder.txvCustomer.setText(customer.getFirstname()+" "+customer.getLastname());
        holder.txvMethod.setText(method.getName());
        holder.txvDate.setText(orders.get(position).getDate());
        holder.setListener(this);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }

    }

    public static class OrderViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        CardView crvOrder;
        TextView txvCustomer;
        TextView txvMethod;
        TextView txvDate;
        ImageButton btnEdit;
        ImageButton btnDelete;
        ImageButton btnDetail;
        View.OnClickListener listener;




        public OrderViewHolder(View itemView) {
            super(itemView);
            crvOrder=(CardView) itemView.findViewById(R.id.crv_order);
            txvCustomer=(TextView)itemView.findViewById(R.id.txv_product);
            txvMethod=(TextView)itemView.findViewById(R.id.txv_quantity);
            txvDate=(TextView)itemView.findViewById(R.id.txv_price);
            btnEdit =(ImageButton) itemView.findViewById(R.id.btn_edit);
            btnDelete =(ImageButton) itemView.findViewById(R.id.btn_delete);
            btnDetail =(ImageButton) itemView.findViewById(R.id.btn_detail);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
            btnDetail.setOnClickListener(this);
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
