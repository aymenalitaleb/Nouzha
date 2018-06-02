package esi.siw.nouzha;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import esi.siw.nouzha.common.Common;
import esi.siw.nouzha.interfaces.ItemClickListener;
import esi.siw.nouzha.models.Category;
import esi.siw.nouzha.models.CurrentSettings;
import esi.siw.nouzha.service.ListenNotification;
import esi.siw.nouzha.viewHolder.CategoryViewHolder;
import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category, currentSettings, favourite, categoryUser;


    TextView txtFullName;

    RecyclerView recycler_categories;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;


    DatabaseReference table_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.categories);
        setSupportActionBar(toolbar);


        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        currentSettings = database.getReference("Current_Settings");
        favourite = database.getReference("userCategory");
        categoryUser = database.getReference("categoryUser");

        // Init Paper
        Paper.init(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ticketIntent = new Intent(Home.this, Ticket.class);
                startActivity(ticketIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set Name for user

        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        String fullName = Common.currentUser.getLastname() + " " + Common.currentUser.getFirstname();
        txtFullName.setText(fullName);

        //Load Menu

        recycler_categories = findViewById(R.id.recycler_categories);
        recycler_categories.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_categories.setLayoutManager(layoutManager);

//        if ( Common.isConnectedToInternet(getBaseContext())) {
        loadCategories();
//        } else {
//            Toast.makeText(this, R.string.check_connection, Toast.LENGTH_SHORT).show();
//        }


        // Call service
        Intent service = new Intent(Home.this, ListenNotification.class);
        startService(service);
    }

    private void loadCategories() {

        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class,
                R.layout.categorie_item,
                CategoryViewHolder.class,
                category) {
            @Override
            protected void populateViewHolder(final CategoryViewHolder viewHolder, Category model, final int position) {
                viewHolder.txtCategoryName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.category_image);
                final Category clickItem = model;

                final ImageView fav = viewHolder.fav;
                fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DatabaseReference categories = favourite.child(Common.currentUser.getPhone());
                        final DatabaseReference users = categoryUser.child(adapter.getRef(position).getKey());

                        categories.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (fav.getContentDescription().equals("non")) {
                                    categories.child(adapter.getRef(position).getKey()).setValue("true");
                                    users.child(Common.currentUser.getPhone()).setValue("true");
                                    Toast.makeText(Home.this, R.string.added_to_favourites, Toast.LENGTH_SHORT).show();
                                    fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                                    fav.setContentDescription("oui");
                                } else {
                                    categories.child(adapter.getRef(position).getKey()).removeValue();
                                    users.child(Common.currentUser.getPhone()).removeValue();
                                    Toast.makeText(Home.this, R.string.removed_from_favourites, Toast.LENGTH_SHORT).show();
                                    fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                    fav.setContentDescription("non");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

                final ArrayList<String> userFavourite = new ArrayList<>();
                DatabaseReference caregories = favourite.child(Common.currentUser.getPhone());
                caregories.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            userFavourite.add(data.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                favourite.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (userFavourite.contains(adapter.getRef(position).getKey())) {
                            viewHolder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                            fav.setContentDescription("oui");
                        } else {
                            viewHolder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            fav.setContentDescription("non");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //get Category and send to new activity
                        Intent activitiesList = new Intent(Home.this, ActivitiesList.class);

                        //Because CategoryId is key, so we just get  key of this item
                        activitiesList.putExtra("categoryId", adapter.getRef(position).getKey());
                        startActivity(activitiesList);

                    }
                });
            }
        };
        recycler_categories.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_current_setting:
                Toast.makeText(Home.this, "Current settings", Toast.LENGTH_LONG).show();
                insertCurrentSettings();
                return true;
//            case R.id.menu_chercher:
//                Toast.makeText(Mon3Activity.this,"Chercher",Toast.LENGTH_LONG).show();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void insertCurrentSettings() {

        LinearLayout layout = new LinearLayout(getBaseContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Insert your current settings");
        final EditText currentBudget = new EditText(Home.this);
        currentBudget.setInputType(InputType.TYPE_CLASS_NUMBER);
        currentBudget.setHint("Budget");
        final EditText freeTimeFrom = new EditText(Home.this);
        freeTimeFrom.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
        freeTimeFrom.setHint("From");
        final EditText freeTimeTo = new EditText(Home.this);
        freeTimeTo.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
        freeTimeTo.setHint("To");
        final EditText freeDate = new EditText(Home.this);
        freeDate.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        freeDate.setHint("Date");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        layout.addView(currentBudget);
        layout.addView(freeTimeFrom);
        layout.addView(freeTimeTo);
        layout.addView(freeDate);

        alertDialog.setView(layout);
        alertDialog.setIcon(R.drawable.ic_settings_black_24dp);

        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Create new request
                CurrentSettings cSettings = new CurrentSettings(
                        currentBudget.getText().toString(),
                        freeTimeFrom.getText().toString(),
                        freeTimeTo.getText().toString(),
                        freeDate.getText().toString(),
                        Common.currentUser.getPhone(),
                        Calendar.getInstance().getTime().toString()
                );

                //submit to firebase
                // we will using System.CurrentTimeMillis as a key
                currentSettings.child(String.valueOf(System.currentTimeMillis())).setValue(cSettings);
                Toast.makeText(Home.this, "Settings saved", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_findActivities) {
            // Handle the camera action
        } else if (id == R.id.nav_favorites) {

        } else if (id == R.id.nav_my_tickets) {
            Intent ticketIntent = new Intent(Home.this, Ticket.class);
            startActivity(ticketIntent);

        } else if (id == R.id.nav_orders) {
            Intent orderIntent = new Intent(Home.this, OrderStatus.class);
            startActivity(orderIntent);

        } else if (id == R.id.nav_statistics) {

        } else if (id == R.id.log_out) {

            // Delete session
            Paper.book().destroy();
            //Logout
            Intent signIn = new Intent(Home.this, SignIn.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_change_password) {
            showChangePasswordDialog();
        } else if (id == R.id.nav_share) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void showChangePasswordDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("CHANGE PASSWORD");
        alertDialog.setMessage("please fill all information");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_pwd = inflater.inflate(R.layout.change_password_layout, null);
        final EditText edtPassword = layout_pwd.findViewById(R.id.edtPassword);
        final EditText edtNewPassword = layout_pwd.findViewById(R.id.edtNewPassword);
        final EditText edtConfirmPassword = layout_pwd.findViewById(R.id.edtConfirmPassword);

        alertDialog.setView(layout_pwd);


        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Change password here

                final android.app.AlertDialog waitingDialog = new SpotsDialog(Home.this);
                waitingDialog.show();

                //Check old password
                if (edtPassword.getText().toString().equals(Common.currentUser.getPassword())) {
                    //Check new password
                    if (edtNewPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
                        Map<String, Object> passwordUpdate = new HashMap<>();
                        passwordUpdate.put("Password", edtNewPassword.getText().toString());

                        //Make update

                        DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
                        user.child(Common.currentUser.getPhone())
                                .updateChildren(passwordUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        waitingDialog.dismiss();
                                        Toast.makeText(Home.this, "Passowrd updated", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Home.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        waitingDialog.dismiss();
                        Toast.makeText(Home.this, "New password doesn't match", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    waitingDialog.dismiss();
                    Toast.makeText(Home.this, "Wrong old passowrd", Toast.LENGTH_SHORT).show();

                }

            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }
}
