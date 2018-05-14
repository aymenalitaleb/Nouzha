package esi.siw.nouzha;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import esi.siw.nouzha.models.Activity;

public class ActivityDetails extends AppCompatActivity {

    TextView activity_name, activity_price, activity_description, activity_date, activity_time_from, activity_time_to, activity_nb_place, activity_adress;
    ImageView activity_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnGoing;
    ElegantNumberButton numberButton;

    String activityId = "";

    FirebaseDatabase database;
    DatabaseReference activities;

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
            activityId = getIntent().getStringExtra("ActivityId");
        if (!activityId.isEmpty()) {
            getDetailsActivity(activityId);
        }

    }

    private void getDetailsActivity(final String activityId) {
        activities.child(activityId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Activity activity = dataSnapshot.getValue(Activity.class);
                Picasso.with(getBaseContext()).load(activity.getImage()).into(activity_image);
                collapsingToolbarLayout.setTitle(activity.getDesignation());
                activity_price.setText(activity.getPrix());
                activity_name.setText(activity.getDesignation());
                activity_description.setText(activity.getDescription());
                activity_date.setText(activity.getDate());
                activity_time_from.setText(activity.getTime_from());
                activity_time_to.setText(activity.getTime_to());
                activity_nb_place.setText(activity.getNbPlaces());
                String activityAdresse = activity.getNumber() + ", " + activity.getStreet() + ", " + activity.getCity();
                activity_adress.setText(activityAdresse);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
