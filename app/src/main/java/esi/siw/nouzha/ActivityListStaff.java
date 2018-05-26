package esi.siw.nouzha;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import esi.siw.nouzha.common.CommonStaff;
import esi.siw.nouzha.interfaces.ItemClickListener;
import esi.siw.nouzha.models.Activity;
import esi.siw.nouzha.viewHolder.ActivityStaffViewHolder;

public class ActivityListStaff extends AppCompatActivity {

    RecyclerView recyler_activity;
    RecyclerView.LayoutManager layoutManager;
    RelativeLayout rootLayout;
    FloatingActionButton fab;


    //Firebase
    FirebaseDatabase database;
    DatabaseReference activityList;
    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId = "";
    FirebaseRecyclerAdapter<Activity, ActivityStaffViewHolder> adapter;

    //Add new  Activity
    EditText edtDesignation, edtnbPlace, edtDescription, edtDate, edtTime_from, edtTime_to, edtPrice, edtDiscount, edtCity, edtStreet, edtStreetNumber, edtZipCode;
    Button btnSelect, btnUpload;

    Activity newActivity;
    Uri imageURI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_list_staff);

        //Firebase
        database = FirebaseDatabase.getInstance();
        activityList = database.getReference("Activity");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        //Init

        recyler_activity = findViewById(R.id.recyler_activity);
        recyler_activity.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyler_activity.setLayoutManager(layoutManager);


        rootLayout = findViewById(R.id.rootLayout);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddActivityDialog();
            }
        });
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("categoryId");
            if (!categoryId.isEmpty()) {
                loadListActivity(categoryId);
            }
        }
    }

    private void showAddActivityDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityListStaff.this);
        alertDialog.setTitle("add new Activity");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_category_layout = inflater.inflate(R.layout.add_new_activity_layout, null);

        edtDesignation = add_category_layout.findViewById(R.id.edtDesignation);
        edtnbPlace = add_category_layout.findViewById(R.id.edtnbPlace);
        edtDescription = add_category_layout.findViewById(R.id.edtDescription);
        edtDate = add_category_layout.findViewById(R.id.edtDate);
        edtTime_from = add_category_layout.findViewById(R.id.edtTime_from);
        edtTime_to = add_category_layout.findViewById(R.id.edtTime_to);
        edtPrice = add_category_layout.findViewById(R.id.edtPrice);
        edtCity = add_category_layout.findViewById(R.id.edtCity);
        edtStreet = add_category_layout.findViewById(R.id.edtStreet);
        edtStreetNumber = add_category_layout.findViewById(R.id.edtStreetNumber);
        edtDiscount = add_category_layout.findViewById(R.id.edtDiscount);
        edtZipCode = add_category_layout.findViewById(R.id.edtZipCode);
        btnSelect = add_category_layout.findViewById(R.id.btnSelect);
        btnUpload = add_category_layout.findViewById(R.id.btnUpload);

        //Event for buttons
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();// Let user choose image from gallery and save its URI
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        alertDialog.setView(add_category_layout);
        alertDialog.setIcon(R.drawable.ic_folder_special_black_24dp);

        //SetButton
        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                //Here , just create new category
                if (newActivity != null) {
                    activityList.push().setValue(newActivity);
                    Snackbar.make(rootLayout, "New Activity " + newActivity.getDesignation() + " was added", Snackbar.LENGTH_SHORT).show();
                }
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

    private void uploadImage() {
        if (imageURI != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(ActivityListStaff.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //set value for new category if image uploaded and we can get it download link
                            newActivity = new Activity();
                            newActivity.setDesignation(edtDesignation.getText().toString());
                            newActivity.setDescription(edtDescription.getText().toString());
                            newActivity.setNbPlaces(edtnbPlace.getText().toString());
                            newActivity.setDate(edtDate.getText().toString());
                            newActivity.setTime_from(edtTime_from.getText().toString());
                            newActivity.setTime_to(edtTime_to.getText().toString());
                            newActivity.setPrix(edtPrice.getText().toString());
                            newActivity.setCity(edtCity.getText().toString());
                            newActivity.setStreet(edtStreet.getText().toString());
                            newActivity.setNumber(edtStreetNumber.getText().toString());
                            newActivity.setDiscount(edtDiscount.getText().toString());
                            newActivity.setZipCode(edtZipCode.getText().toString());
                            newActivity.setImage(uri.toString());
                            newActivity.setCategoryId(categoryId);

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(ActivityListStaff.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Upload " + progress + " %");
                }
            });

        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), CommonStaff.PICK_IMAGE_REQUEST);

    }


    private void loadListActivity(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Activity, ActivityStaffViewHolder>(
                Activity.class,
                R.layout.activity_item_staff,
                ActivityStaffViewHolder.class,
                activityList.orderByChild("categoryId").equalTo(categoryId)
        ) {
            @Override
            protected void populateViewHolder(ActivityStaffViewHolder viewHolder, Activity model, int position) {
                viewHolder.activity_name.setText(model.getDesignation());
                Picasso.with(getBaseContext())
                        .load(model.getImage())
                        .into(viewHolder.activity_image);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // COde late
                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        recyler_activity.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommonStaff.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageURI = data.getData();
            btnSelect.setText("Image selected !");

        }
    }
}
