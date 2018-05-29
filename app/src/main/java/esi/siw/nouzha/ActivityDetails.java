package esi.siw.nouzha;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import esi.siw.nouzha.common.Common;
import esi.siw.nouzha.database.Database;
import esi.siw.nouzha.models.Activity;
import esi.siw.nouzha.models.Order;

public class ActivityDetails extends AppCompatActivity {

    TextView activity_name, activity_price, activity_description, activity_date, activity_time_from, activity_time_to, activity_nb_place, activity_adress;
    ImageView activity_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnGoing;
    ElegantNumberButton numberButton;

    String activityId = "";

    FirebaseDatabase database;
    DatabaseReference activities;

    Activity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Firebase
        database = FirebaseDatabase.getInstance();
        activities = database.getReference("Activity");

        //Init View

        numberButton = findViewById(R.id.number_button);
        btnGoing = findViewById(R.id.btnGoing);
        btnGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToTicket(new Order(
                        activityId,
                        currentActivity.getDesignation(),
                        numberButton.getNumber(),
                        currentActivity.getPrix(),
                        currentActivity.getDiscount()
                ));

                Toast.makeText(ActivityDetails.this, "Added to My tickets", Toast.LENGTH_SHORT).show();
            }
        });

        activity_description = findViewById(R.id.activity_description);
        activity_price = findViewById(R.id.activity_price);
        activity_name = findViewById(R.id.activity_name);
        activity_time_from = findViewById(R.id.activity_time_from);
        activity_time_to = findViewById(R.id.activity_time_to);
        activity_nb_place = findViewById(R.id.activity_nb_place);
        activity_date = findViewById(R.id.activity_date);
        activity_image = findViewById(R.id.img_activity);
        activity_adress = findViewById(R.id.activity_adress);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get Activity id from Intent
        if (getIntent() != null)
            activityId = getIntent().getStringExtra("activityId");
        if (!activityId.isEmpty()) {
            if (Common.isConnectedToInternet(getBaseContext())) {
                getDetailsActivity(activityId);
            } else {
                Toast.makeText(this, "Please check your internet connection !", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void getDetailsActivity(final String activityId) {
        activities.child(activityId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentActivity = dataSnapshot.getValue(Activity.class);

                //Set Image
                Picasso.with(getBaseContext()).load(currentActivity.getImage()).into(activity_image);
                collapsingToolbarLayout.setTitle(currentActivity.getDesignation());
                activity_price.setText(currentActivity.getPrix());
                activity_name.setText(currentActivity.getDesignation());
                activity_description.setText(currentActivity.getDescription());
                activity_date.setText(currentActivity.getDate());
                activity_time_from.setText(currentActivity.getTime_from());
                activity_time_to.setText(currentActivity.getTime_to());
                activity_nb_place.setText(currentActivity.getNbPlaces());
                String activityAdresse = currentActivity.getNumber() + ", " + currentActivity.getStreet() + ", " + currentActivity.getCity();
                activity_adress.setText(activityAdresse);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
