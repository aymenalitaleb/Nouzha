package esi.siw.nouzha;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

import esi.siw.nouzha.common.Common;
import esi.siw.nouzha.database.Database;
import esi.siw.nouzha.models.Activity;
import esi.siw.nouzha.models.Order;
import esi.siw.nouzha.models.Rating;

public class ActivityDetails extends AppCompatActivity implements RatingDialogListener {

    private GoogleMap mMap;
    public static Double latitude, longitude;
    public static String designation;

    TextView activity_name, activity_price, activity_description, activity_date, activity_time_from, activity_time_to, activity_nb_place, activity_adress;
    ImageView activity_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnGoing, btnRating;
    ElegantNumberButton numberButton;
    RatingBar ratingBar;

    String activityId = "";

    FirebaseDatabase database;
    DatabaseReference activities;
    DatabaseReference ratingTable;

    Activity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Firebase
        database = FirebaseDatabase.getInstance();
        activities = database.getReference("Activity");
        ratingTable = database.getReference("Rating");


        //Map
        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = MapFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mapContent, fragment).commit();


        //Init View

        numberButton = findViewById(R.id.number_button);
        btnGoing = findViewById(R.id.btnGoing);
        btnRating = findViewById(R.id.btnRating);
        ratingBar = findViewById(R.id.ratingBar);

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRatingDialog();
            }
        });

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

                Toast.makeText(ActivityDetails.this, R.string.added_to_my_tickets, Toast.LENGTH_SHORT).show();
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
        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                activityId = getIntent().getStringExtra("activityId");
            }

        }

        if (!activityId.isEmpty()) {
//           if (Common.isConnectedToInternet(getBaseContext())) {
            getDetailsActivity(activityId);
            getRatingActivity(activityId);
//            } else {
//                Toast.makeText(this, R.string.check_connection, Toast.LENGTH_SHORT).show();
//           }

        }

    }
    private void getRatingActivity(String activityId) {
        com.google.firebase.database.Query activityRating = ratingTable.orderByChild("activityId").equalTo(activityId);
        activityRating.addValueEventListener(new ValueEventListener() {
            float count = 0, sum = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;
                }
                if (count != 0) {
                    float average = sum / count;
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Quite Ok", "Very Good", "Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this activity")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadAnim)
                .create(ActivityDetails.this)
                .show();
    }


    private void getDetailsActivity(final String activityId) {
        activities.child(activityId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentActivity = dataSnapshot.getValue(Activity.class);

                //Set Image
                Picasso.with(getBaseContext()).load(currentActivity.getImage()).into(activity_image);
                collapsingToolbarLayout.setTitle(currentActivity.getDesignation());
                designation = currentActivity.getDesignation();
                activity_price.setText(currentActivity.getPrix());
                activity_name.setText(currentActivity.getDesignation());
                activity_description.setText(currentActivity.getDescription());
                activity_date.setText(currentActivity.getDate());
                activity_time_from.setText(currentActivity.getTime_from());
                activity_time_to.setText(currentActivity.getTime_to());
                activity_nb_place.setText(currentActivity.getNbPlaces());
                latitude = Double.valueOf(currentActivity.getLatitude());
                longitude = Double.valueOf(currentActivity.getLongitude());
                String activityAdresse = currentActivity.getNumber() + ", " + currentActivity.getStreet() + ", " + currentActivity.getCity();
                activity_adress.setText(activityAdresse);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onPositiveButtonClicked(int value, String comments) {
        //Get rating and upload it to firebase
        final Rating rating = new Rating(Common.currentUser.getPhone(),
                activityId,
                String.valueOf(value),
                comments);
        ratingTable.child(Common.currentUser.getPhone()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Common.currentUser.getPhone()).exists()) {
                    //remove old value
                    ratingTable.child(Common.currentUser.getPhone()).removeValue();
                    //Update new value
                    ratingTable.child(Common.currentUser.getPhone()).setValue(rating);
                } else {
                    //Update new value
                    ratingTable.child(Common.currentUser.getPhone()).setValue(rating);
                }
                Toast.makeText(ActivityDetails.this, "Thank you for submit rating !!!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }
}


