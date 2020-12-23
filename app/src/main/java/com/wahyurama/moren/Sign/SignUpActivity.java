package com.wahyurama.moren.Sign;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wahyurama.moren.App.MainActivity;
import com.wahyurama.moren.Model.ImageModel;
import com.wahyurama.moren.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    // Method Camera
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;
    private EditText mName, mEmail, mPassword, mConfirmPassword;
    private MaterialButton mSignUp;
    private ImageView mIconBack, ivCreateAccount;
    private ImageButton mAddPicture;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private StorageReference mstorage;
    private DatabaseReference mDatabase;

    private boolean clickImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mName = findViewById(R.id.inputName);
        mEmail = findViewById(R.id.inputEmail);
        mPassword = findViewById(R.id.inputPassword);
        mConfirmPassword = findViewById(R.id.inputConfirmPassword);
        mSignUp = findViewById(R.id.buttonSignUp);
        mIconBack = findViewById(R.id.icBack);
        mAddPicture = findViewById(R.id.btn_add_picture);
        ivCreateAccount = findViewById(R.id.iv_create_account);
        progressBar = findViewById(R.id.progressBarUpload);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mstorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mIconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),
                        SignInActivity.class));
            }
        });

        // Add image
        mAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickImage = true;
                if (ContextCompat.checkSelfPermission(SignUpActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SignUpActivity.this,
                            new String[]{
                                    Manifest.permission.CAMERA
                            }, REQUEST_CODE_CAMERA);
                }
                getImageFromDevice();
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = mName.getText().toString();
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString().trim();
                final String confirmPass =
                        mConfirmPassword.getText().toString().trim();

                if (!clickImage) {
                    Snackbar snackbar = Snackbar
                            .make(view, "Please select a image",
                                    Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(Color.RED);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();
                    return;
                } else if (TextUtils.isEmpty(name)) {
                    mName.setError("Name field is required!");
                    mName.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email field is required!");
                    mEmail.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password field is required!");
                    mPassword.requestFocus();
                    return;
                } else if (password.length() < 6) {
                    mPassword.setError("Password must be more than 6 " +
                            "characters!");
                    mPassword.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(confirmPass)) {
                    mConfirmPassword.setError("Confirm password field is " +
                            "required!");
                    mConfirmPassword.requestFocus();
                    return;
                } else if (!password.equals(confirmPass)) {
                    mConfirmPassword.setError("Password is not match!");
                    mConfirmPassword.requestFocus();
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String userID =
                                    Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                            DocumentReference documentReference =
                                    mStore.collection("users").document(userID);

                            Map<String, Object> user =
                                    new HashMap<>();
                            user.put("fullName", name);
                            user.put("email", email);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "user profile " +
                                            "created " + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "user profile " +
                                            "fail " + e.getMessage());
                                }
                            });
                            uploadImage();

                        } else {
                            Toast.makeText(SignUpActivity.this, "Error ! " +
                                            task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
    }

    private void uploadImage() {
        ivCreateAccount.setDrawingCacheEnabled(true);
        ivCreateAccount.buildDrawingCache();
        Bitmap bitmap =
                ((BitmapDrawable) ivCreateAccount.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();


        String fileName = Objects.requireNonNull(mAuth.getCurrentUser()).getUid() + ".jpg";
        String pathImage = "images/" + fileName;
        UploadTask uploadTask = mstorage.child(pathImage).putBytes(bytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mstorage.child(pathImage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        String uid = mAuth.getCurrentUser().getUid();
                        mDatabase.child("images")
                                .child(uid)
                                .setValue(new ImageModel(downloadUrl));
                        Toast.makeText(SignUpActivity.this, "Account " +
                                        "created",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, "Upload Failed!",
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
                            .into(ivCreateAccount);
                    mAddPicture.setVisibility(View.GONE);
                }
                break;

            case REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();

                    Glide.with(this).
                            load(uri)
                            .apply(RequestOptions.circleCropTransform())
                            .into(ivCreateAccount);
                    mAddPicture.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),
                SignInActivity.class));
    }
}