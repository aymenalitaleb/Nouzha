package esi.siw.nouzha;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

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

    //Search Functionality
    FirebaseRecyclerAdapter<Activity, ActivityViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    //Facebook Share
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    SwipeRefreshLayout swipeRefreshLayout;

    //Create target from Picasso
    Target  target = new Target() {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            //Create Photo from BitMap
            SharePhoto  photo = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            if(ShareDialog.canShow(SharePhotoContent.class)){
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                 shareDialog.show(content);
            }

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_list);


        //Init Facebook
        callbackManager=CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);



        //Firebase
        database = FirebaseDatabase.getInstance();
        activitiesList = database.getReference("Activity");

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
                );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //        if ( Common.isConnectedToInternet(getBaseContext())) {
                //Get Intent here
                if (getIntent() != null)
                    categoryId = getIntent().getStringExtra("categoryId");
                if (!categoryId.isEmpty() && categoryId != null) {
//               if (Common.isConnectedToInternet(getBaseContext())) {
                    laodListActivities(categoryId);
//                } else {
//                    Toast.makeText(this, R.string.check_connection, Toast.LENGTH_SHORT).show();
//                }

                }

//        } else {
                //    Toast.makeText(getBaseContext(), R.string.check_connection, Toast.LENGTH_SHORT).show();
//        }

            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //        if ( Common.isConnectedToInternet(getBaseContext())) {
                //Get Intent here
                if (getIntent() != null)
                    categoryId = getIntent().getStringExtra("categoryId");
                if (!categoryId.isEmpty() && categoryId != null) {
//               if (Common.isConnectedToInternet(getBaseContext())) {
                    laodListActivities(categoryId);
//                } else {
//                    Toast.makeText(getBaseContext(), R.string.check_connection, Toast.LENGTH_SHORT).show();
//                }

                }

//        } else {
                //    Toast.makeText(getBaseContext(), R.string.check_connection, Toast.LENGTH_SHORT).show();
//        }
            }
        });




        recycler_activities = findViewById(R.id.recyler_activity);
        recycler_activities.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_activities.setLayoutManager(layoutManager);


        //Search
        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter your activity");
        // materialSearchBar.setSpeechMode(false);
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // when user type a text , we will change sugget list

                List<String> suggest = new ArrayList<>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //when search bar is close, restore original  suggest adapter
                if (!enabled)
                    recycler_activities.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //when search finish, show result of seatch adapter
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Activity, ActivityViewHolder>(
                Activity.class,
                R.layout.activity_item,
                ActivityViewHolder.class,
                activitiesList.orderByChild("designation").equalTo(text.toString())) {

            @Override
            protected void populateViewHolder(ActivityViewHolder viewHolder, Activity model, int position) {
                viewHolder.txtActivityName.setText(model.getDesignation());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.activity_image);
                final Activity clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent activityDetails = new Intent(ActivitiesList.this, ActivityDetails.class);
                        activityDetails.putExtra("activityId", searchAdapter.getRef(position).getKey());
                        startActivity(activityDetails);
                    }
                });

            }
        };
        recycler_activities.setAdapter(searchAdapter);
    }


    private void loadSuggest() {
        activitiesList.orderByChild("categoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Activity item = postSnapshot.getValue(Activity.class);
                    suggestList.add(item.getDesignation());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void laodListActivities(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Activity, ActivityViewHolder>(Activity.class,
                R.layout.activity_item,
                ActivityViewHolder.class,
                activitiesList.orderByChild("categoryId").equalTo(categoryId)) //select  * from Activity where CategoryId = categoryid
        {
            @Override
            protected void populateViewHolder(ActivityViewHolder viewHolder, final Activity model, int position) {
                viewHolder.txtActivityName.setText(model.getDesignation());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.activity_image);
                final Activity local = model;

                viewHolder.share_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Picasso.with(getApplicationContext())
                                .load(model.getImage())
                                .into(target);
                        Toast.makeText(ActivitiesList.this, "Share to facebook ", Toast.LENGTH_SHORT).show();
                    }
                });

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new Activity

                        Intent activityDetails = new Intent(ActivitiesList.this, ActivityDetails.class);
                        activityDetails.putExtra("activityId", adapter.getRef(position).getKey());//Send activity Id to new activity
                        startActivity(activityDetails);
                    }
                });

            }
        };

        //Set adapter
//        Log.d("TAG",""+adapter.getItemCount());
        recycler_activities.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }
}
