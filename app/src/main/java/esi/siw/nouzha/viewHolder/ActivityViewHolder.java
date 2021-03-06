package esi.siw.nouzha.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import esi.siw.nouzha.interfaces.ItemClickListener;
import esi.siw.nouzha.R;

public class ActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtActivityName;
    public ImageView activity_image,share_image;

    private ItemClickListener itemClickListener;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;


    }

    public ActivityViewHolder(View itemView) {
        super(itemView);

        txtActivityName = itemView.findViewById(R.id.activity_name);
        activity_image = itemView.findViewById(R.id.activity_image);
        share_image = itemView.findViewById(R.id.btnShare);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
