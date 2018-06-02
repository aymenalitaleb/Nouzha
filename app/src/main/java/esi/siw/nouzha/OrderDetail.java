package esi.siw.nouzha;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import esi.siw.nouzha.common.Common;
import esi.siw.nouzha.viewHolder.OrderDetailAdapter;

public class OrderDetail extends AppCompatActivity {

    TextView order_id,order_phone,order_city,order_total,order_comment;
    String order_id_value="";
    RecyclerView listActivities;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order_id =findViewById(R.id.order_id);
        order_phone =findViewById(R.id.order_phone);
        order_city =findViewById(R.id.order_city);
        order_total =findViewById(R.id.order_total);
        order_comment =findViewById(R.id.order_comment);

        listActivities = findViewById(R.id.listActivities);
        listActivities.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listActivities.setLayoutManager(layoutManager);

        if(getIntent() != null){
            order_id_value = getIntent().getStringExtra("OrderId");

            //set value
            order_id.setText(order_id_value);
            order_phone.setText(Common.currentRequest.getPhone());
            order_city.setText(Common.currentRequest.getCity());
            order_total.setText(Common.currentRequest.getTotal());
            order_comment.setText(Common.currentRequest.getComment());

            OrderDetailAdapter adapter =  new OrderDetailAdapter(Common.currentRequest.getActivities());
            adapter.notifyDataSetChanged();
            listActivities.setAdapter(adapter);

        }

    }
}
