package com.own.sqlite1.adapter;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.own.sqlite1.R;
import com.own.sqlite1.model.MethodPayment;

import java.util.List;


public class MethodAdapter extends RecyclerView.Adapter <MethodAdapter.MethodViewHolder>
        implements View.OnClickListener {

    List<MethodPayment> methodsPayment;
    View.OnClickListener listener;
    //Constructor
    public MethodAdapter(List<MethodPayment> methods){
        this.methodsPayment = methods;
    }
//getter and setter de listener
    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MethodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       //se infla el cardview al reciclerview
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.method_cell_layout,parent,false);
        MethodViewHolder holder=new MethodViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(MethodViewHolder holder, int position) {
        holder.txvName.setText(methodsPayment.get(position).getName());
        holder.setListener(this);

    }

    @Override
    public int getItemCount() {
        return methodsPayment.size();
    }
    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }

    }

    public static class MethodViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        CardView crvMethod;
        TextView txvName;
        ImageButton btnEdit;
        ImageButton btnDelete;
        View.OnClickListener listener;




        public MethodViewHolder(View itemView) {
            super(itemView);
            crvMethod=(CardView) itemView.findViewById(R.id.crv_method);
            txvName=(TextView)itemView.findViewById(R.id.txv_name);
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
