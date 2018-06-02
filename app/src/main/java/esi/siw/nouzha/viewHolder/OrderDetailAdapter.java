package esi.siw.nouzha.viewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import esi.siw.nouzha.R;
import esi.siw.nouzha.models.Order;

class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView name, quantity, price, discount;


    public MyViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.activity_name);
        quantity = itemView.findViewById(R.id.activity_quantity);
        price = itemView.findViewById(R.id.activity_price);
        discount = itemView.findViewById(R.id.activity_discount);

    }
}

public class OrderDetailAdapter extends RecyclerView.Adapter<MyViewHolder> {

    List<Order> myOrders;

    public OrderDetailAdapter(List<Order> myOrders) {
        this.myOrders = myOrders;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = myOrders.get(position);
        holder.name.setText(String.format("Name : %s", order.getActivityName()));
        holder.quantity.setText(String.format("Quantity : %s", order.getQuantity()));
        holder.price.setText(String.format("Price : %s", order.getPrice()));
        holder.discount.setText(String.format("Discount : %s", order.getDiscount()));
    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}
