package esi.siw.nouzha.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import esi.siw.nouzha.R;
import esi.siw.nouzha.common.CommonStaff;
import esi.siw.nouzha.interfaces.ItemClickListener;

public class CategoryStaffViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {

    public TextView txtCategoryName;
    public ImageView category_image;

    private ItemClickListener itemClickListener;

    public CategoryStaffViewHolder(View itemView) {
        super(itemView);
        txtCategoryName = itemView.findViewById(R.id.category_name);
        category_image= itemView.findViewById(R.id.category_image);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), CommonStaff.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(), CommonStaff.DELETE);

    }
}
