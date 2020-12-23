package com.wahyurama.moren.App;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wahyurama.moren.Model.ImageModel;
import com.wahyurama.moren.R;
import com.wahyurama.moren.Sign.SignInActivity;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AccountActivity extends AppCompatActivity {

    private TextView userName, email, userID;
    private MaterialButton updateBtn;
    private ImageView ivBack, ivEditAccount;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private DatabaseReference myRef;
    private ProgressBar progressBar;
    private String UID;

    private boolean clickImage;

    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        userName = findViewById(R.id.userName);
        email = findViewById(R.id.emailAddress);
        userID = findViewById(R.id.userID);
        FloatingActionButton signOut = findViewById(R.id.floatingActionButton);
        ivBack = findViewById(R.id.ic_back_account);
        ivEditAccount = findViewById(R.id.iv_edit_account);
        ImageButton iv_edit_picture = findViewById(R.id.btn_edit_picture);
        updateBtn = findViewById(R.id.updateButton);
        progressBar = findViewById(R.id.progressBar2);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore mStore = FirebaseFirestore.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        UID = mAuth.getCurrentUser().getUid();

        getImage();

        DocumentReference documentReference =
                mStore.collection("users").document(UID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    userID.setText(UID);
                    userName.setText(snapshot.getString("fullName"));
                    email.setText(snapshot.getString("email"));

                    userName.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.length() == 0) {
                                userName.setError("Your name cannot empty");
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            updateBtn.setVisibility(View.VISIBLE);
                        }
                    });

                } else {
                    Log.d("TAG", "Mo such document :" + task.getException());
                }
            }
        });

        iv_edit_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickImage = true;
                if (ContextCompat.checkSelfPermission(AccountActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AccountActivity.this,
                            new String[]{
                                    Manifest.permission.CAMERA
                            }, REQUEST_CODE_CAMERA);
                }
                getImageFromDevice();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newUser = userName.getText().toString();
                Map<String, Object> user = new HashMap<>();
                user.put("fullName", newUser);

                progressBar.setVisibility(View.VISIBLE);

                documentReference.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            uploadImage();
                        }
                    }
                });
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(AccountActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getImage() {
        Query query =
                myRef.child("images").child(UID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(getApplicationContext())
                        .load(snapshot.child("imageUrl").getValue().toString())
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivEditAccount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccountActivity.this, "Error!" + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getImageFromDevice() {
        CharSequence[] menu = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Upload Image")
                .setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                // Get image from camera
                                Intent imageIntentCamera =
                                        new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(imageIntentCamera,
                                        REQUEST_CODE_CAMERA);
                                break;

                            case 1:
                                // Get image from gallery
                                Intent imageIntentGallery =
                                        new Intent(Intent.ACTION_PICK,
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(imageIntentGallery, REQUEST_CODE_GALLERY);
                                break;
                        }
                    }
                });
        builder.create();
        builder.show();
        updateBtn.setVisibility(View.VISIBLE);
    }

    private void uploadImage() {
        ivEditAccount.setDrawingCacheEnabled(true);
        ivEditAccount.buildDrawingCache();
        Bitmap bitmap =
                ((BitmapDrawable) ivEditAccount.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();


        String fileName = Objects.requireNonNull(mAuth.getCurrentUser()).getUid() + ".jpg";
        String pathImage = "images/" + fileName;
        UploadTask uploadTask = mStorage.child(pathImage).putBytes(bytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mStorage.child(pathImage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        String uid = mAuth.getCurrentUser().getUid();
                        myRef.child("images")
                                .child(uid)
                                .setValue(new ImageModel(downloadUrl));
                        Toast.makeText(AccountActivity.this, "Update " +
                                        "Success",
                                Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AccountActivity.this, "Update Failed!",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                double progress =
                        (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) Objects.requireNonNull(data).getExtras().get("data");
                    Glide.with(getApplicationContext())
                            .load(bitmap)
                            .apply(RequestOptions.circleCropTransform())
                            .into(ivEditAccount);
                }
                break;

            case REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();

                    Glide.with(this).
                            load(uri)
                            .apply(RequestOptions.circleCropTransform())
                            .into(ivEditAccount);
                }
                break;
        }
    }
}