package esi.siw.nouzha;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import esi.siw.nouzha.common.Common;
import esi.siw.nouzha.models.Request;
import esi.siw.nouzha.viewHolder.OrderViewHolder;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Firebase

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());
    }

    private void loadOrders(String phone) {

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {

                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText("");
                viewHolder.txtOrderDate.setText("12/12/2018");
                viewHolder.txtOrderPhone.setText(Common.currentUser.getPhone());


            }
        };
        recyclerView.setAdapter(adapter);
    }

    private String convertDateToStatus(String inputDateString) throws ParseException {
        String status = "";
        Calendar calCurr = Calendar.getInstance();
        Calendar day = Calendar.getInstance();
        day.setTime(new SimpleDateFormat("MM/dd/yyyy").parse(inputDateString));
        if (day.after(calCurr)) {
            status = R.string.days_left+ " " + (day.get(Calendar.DAY_OF_MONTH) - (calCurr.get(Calendar.DAY_OF_MONTH)));
        }
        return status;
    }
}