package esi.siw.nouzha.viewHolder;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import esi.siw.nouzha.R;
import esi.siw.nouzha.interfaces.ItemClickListener;
import esi.siw.nouzha.models.Order;

class TicketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView text_ticket_name, text_Price;
    public ImageView img_ticket_count;

    private ItemClickListener itemClickListener;


    public void setticket_item_name(TextView ticket_item_name) {
        this.text_ticket_name = ticket_item_name;
    }

    public TicketViewHolder(View itemView) {
        super(itemView);
        text_ticket_name = itemView.findViewById(R.id.ticket_item_name);
        text_Price = itemView.findViewById(R.id.ticket_item_Price);
        img_ticket_count = itemView.findViewById(R.id.ticket_item_count);
    }

    @Override
    public void onClick(View view) {

    }
}

public class TicketAdapter extends RecyclerView.Adapter<TicketViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private Context context;

    public TicketAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.ticket_layout, parent, false);
        return new TicketViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TicketViewHolder holder, int position) {
        TextDrawable drawable = TextDrawable.builder().buildRound("" + listData.get(position).getQuantity(), Color.RED);
        holder.img_ticket_count.setImageDrawable(drawable);

        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        float price = (Float.parseFloat(listData.get(position).getPrice())) * (Float.parseFloat(listData.get(position).getQuantity()));
        holder.text_Price.setText(fmt.format(price));
        holder.text_ticket_name.setText(listData.get(position).getActivityName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
