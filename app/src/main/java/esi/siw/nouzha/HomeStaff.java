package esi.siw.nouzha;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import esi.siw.nouzha.common.Common;
import esi.siw.nouzha.common.CommonStaff;
import esi.siw.nouzha.interfaces.ItemClickListener;
import esi.siw.nouzha.models.Category;
import esi.siw.nouzha.viewHolder.CategoryStaffViewHolder;
import io.paperdb.Paper;

public class HomeStaff extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtFullName_staff;

    FirebaseDatabase database;
    DatabaseReference categories;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseRecyclerAdapter<Category, CategoryStaffViewHolder> adapter;

    //View
    RecyclerView recycler_categories;
    RecyclerView.LayoutManager layoutManager;


    // add new category layout
    EditText edtName;
    Button btnSelect, btnUpload;

    Category newCategory;
    Uri imageURI;

    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_staff);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Categories Management");
        setSupportActionBar(toolbar);


        //Init Firebase
        database = FirebaseDatabase.getInstance();
        categories = database.getReference("Category");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Init Paper
        Paper.init(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();//show form of adding a new category
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set name for user
        View headerView = navigationView.getHeaderView(0);
        txtFullName_staff = headerView.findViewById(R.id.txtFullName_staff);
//        String fullName = CommonStaff.currentUser.getFirstname() + " " + CommonStaff.currentUser.getLastname();
        Toast.makeText(HomeStaff.this, "admin", Toast.LENGTH_SHORT).show();
//        txtFullName_staff.setText(fullName);

        //Init View
        recycler_categories = findViewById(R.id.recycler_categories_staff);
        recycler_categories.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_categories.setLayoutManager(layoutManager);

        if (Common.isConnectedToInternet(getBaseContext())) {
            loadCategories();
        } else {
            Toast.makeText(this, "Please check your internet connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeStaff.this);
        alertDialog.setTitle("add new Category");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_category_layout = inflater.inflate(R.layout.add_new_category_layout, null);

        edtName = add_category_layout.findViewById(R.id.edtName);
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
                if (newCategory != null) {
                    categories.push().setValue(newCategory);
                    Snackbar.make(drawer, "New category " + newCategory.getName() + " was added", Snackbar.LENGTH_SHORT).show();
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
                    Toast.makeText(HomeStaff.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //set value for new category if image uploaded and we can get it download link
                            newCategory = new Category(edtName.getText().toString(), uri.toString());

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(HomeStaff.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonStaff.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageURI = data.getData();
            btnSelect.setText("Image selected !");

        }
    }


    private void loadCategories() {
        adapter = new FirebaseRecyclerAdapter<Category, CategoryStaffViewHolder>(
                Category.class,
                R.layout.category_item_staff,
                CategoryStaffViewHolder.class,
                categories
        ) {
            @Override
            protected void populateViewHolder(CategoryStaffViewHolder viewHolder, Category model, int position) {
                viewHolder.txtCategoryName.setText(model.getName());
                Picasso.with(HomeStaff.this).load(model.getImage())
                        .into(viewHolder.category_image);
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Send Categori Id and start new activity
                        Intent activityList = new Intent(HomeStaff.this, ActivityListStaff.class);
                        activityList.putExtra("categoryId", adapter.getRef(position).getKey());
                        startActivity(activityList);

                    }
                });

            }
        };

        adapter.notifyDataSetChanged();//refresh data if have data changed
        recycler_categories.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_staff, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            // Delete session
            Paper.book().destroy();
            //Logout
            Intent signIn = new Intent(HomeStaff.this, SignIn.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Update /Delete Category


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(CommonStaff.UPDATE)) {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        } else if (item.getTitle().equals(CommonStaff.DELETE)) {
            deleteCategory(adapter.getRef(item.getOrder()).getKey());
        }


        return super.onContextItemSelected(item);
    }

    private void deleteCategory(String key) {
        categories.child(key).removeValue();
        Toast.makeText(this, "Category deleted !!", Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialog(final String key, final Category item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeStaff.this);
        alertDialog.setTitle("Update Category");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_category_layout = inflater.inflate(R.layout.add_new_category_layout, null);

        edtName = add_category_layout.findViewById(R.id.edtName);
        btnSelect = add_category_layout.findViewById(R.id.btnSelect);
        btnUpload = add_category_layout.findViewById(R.id.btnUpload);

        //set Default name
        edtName.setText(item.getName());


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
                changeImage(item);
            }
        });

        alertDialog.setView(add_category_layout);
        alertDialog.setIcon(R.drawable.ic_folder_special_black_24dp);

        //SetButton
        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                //Update information
                item.setName(edtName.getText().toString());
                categories.child(key).setValue(item);
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

    private void changeImage(final Category item) {
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
                    Toast.makeText(HomeStaff.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            item.setImage(uri.toString());

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(HomeStaff.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
}
