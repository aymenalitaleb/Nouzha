package esi.siw.nouzha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import esi.siw.nouzha.common.Common;
import esi.siw.nouzha.interfaces.ItemClickListener;
import esi.siw.nouzha.models.Activity;
import esi.siw.nouzha.viewHolder.ActivityViewHolder;

public class ActivitiesList extends AppCompatActivity {

    RecyclerView recycler_activities;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference activitiesList;

    String categoryId = "";

    FirebaseRecyclerAdapter<Activity, ActivityViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_list);


        //Firebase
        database = FirebaseDatabase.getInstance();
        activitiesList = database.getReference("Activity");

        recycler_activities = findViewById(R.id.recyler_activity);
        recycler_activities.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_activities.setLayoutManager(layoutManager);

        //Get Intent here
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("categoryId");
            if (!categoryId.isEmpty() && categoryId != null) {
               if (Common.isConnectedToInternet(getBaseContext())) {
                    laodListActivities(categoryId);
                } else {
                    Toast.makeText(this, R.string.check_connection, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void laodListActivities(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Activity, ActivityViewHolder>(Activity.class,
                R.layout.activity_item,
                ActivityViewHolder.class,
                activitiesList.orderByChild("categoryId").equalTo(categoryId)) //select  * from Activity where CategoryId = categoryid
        {
            @Override
            protected void populateViewHolder(ActivityViewHolder viewHolder, Activity model, int position) {
                viewHolder.txtActivityName.setText(model.getDesignation());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.activity_image);
                final Activity local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new Activity

                        Intent activityDetails = new Intent(ActivitiesList.this, ActivityDetails.class);
                        activityDetails.putExtra("activityId",adapter.getRef(position).getKey());//Send activity Id to new activity
                        startActivity(activityDetails);
                    }
                });

            }
        };

        //Set adapter
//        Log.d("TAG",""+adapter.getItemCount());
        recycler_activities.setAdapter(adapter);
    }
}
