package esi.siw.nouzha;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import esi.siw.nouzha.common.Common;
import esi.siw.nouzha.database.Database;
import esi.siw.nouzha.models.Order;
import esi.siw.nouzha.models.Request;
import esi.siw.nouzha.viewHolder.TicketAdapter;

public class Ticket extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    Button btnGetTicket;

    List<Order> ticket = new ArrayList<>();

    TicketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        //Firebase

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //Init

        recyclerView = findViewById(R.id.listTicket);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        txtTotalPrice = findViewById(R.id.total);
        btnGetTicket = findViewById(R.id.btnGetTicket);

        btnGetTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

        loadListActivity();
    }

    private void showAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Ticket.this);
        alertDialog.setTitle("One more step");
        alertDialog.setMessage("Enter your City: ");

        final EditText edtCity = new EditText(Ticket.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtCity.setLayoutParams(lp);
        alertDialog.setView(edtCity);//Add edit text to dialog
        alertDialog.setIcon(R.drawable.ic_folder_special_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Create new request
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getFirstname() + " " + Common.currentUser.getLastname(),
                        edtCity.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        ticket
                );

                //submit to firebase
                // we will using System.CurrentTimeMillis as a key
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                //Delete ticket
                new Database(getBaseContext()).cleanTicket();
                Toast.makeText(Ticket.this, "Order placed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private void loadListActivity() {
        ticket = new Database(this).getTickets();
        adapter = new TicketAdapter(ticket, this);
        recyclerView.setAdapter(adapter);

        // calcule total price
        float total = 0;
        for (Order order : ticket) {
            total += (Float.parseFloat(order.getPrice())) * (Float.parseFloat(order.getQuantity()));
            Locale locale = new Locale("en", "US");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            txtTotalPrice.setText(fmt.format(total));
        }


    }
}









