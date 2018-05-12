package esi.siw.nouzha.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import esi.siw.nouzha.Interface.ItemClickListener;
import esi.siw.nouzha.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtCategoryName;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        txtCategoryName = itemView.findViewById(R.id.category_name);
        imageView = itemView.findViewById(R.id.category_image);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {

            itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
