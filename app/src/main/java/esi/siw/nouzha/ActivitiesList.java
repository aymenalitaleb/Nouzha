package esi.siw.nouzha;

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

import esi.siw.nouzha.Entities.Activity;
import esi.siw.nouzha.Interface.ItemClickListener;
import esi.siw.nouzha.ViewHolder.ActivityViewHolder;

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
            categoryId = getIntent().getStringExtra("CategoryId");
            if (!categoryId.isEmpty() && categoryId != null) {
                laodListActivities(categoryId);
            }
        }
    }

    private void laodListActivities(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Activity, ActivityViewHolder>(Activity.class,
                R.layout.activity_item,
                ActivityViewHolder.class,
                activitiesList.orderByChild("CategoryId").equalTo(categoryId)) //select  * from Activity where CategoryId = categoryid
        {
            @Override
            protected void populateViewHolder(ActivityViewHolder viewHolder, Activity model, int position) {
                viewHolder.txtActivityName.setText(model.getDesignation());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.activity_image);
                final Activity local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(ActivitiesList.this, "" + local.getDesignation(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };

        //Set adapter
//        Log.d("TAG",""+adapter.getItemCount());
        recycler_activities.setAdapter(adapter);
    }
}
