package com.wahyurama.moren.Sign;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wahyurama.moren.MainActivity;
import com.wahyurama.moren.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private EditText mName, mEmail, mPassword, mConfirmPassword;
    private MaterialButton mSignUp;
    private ImageView mIconBack;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;

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

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        mIconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),
                        SignInActivity.class));
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

                if (TextUtils.isEmpty(name)) {
                    mName.setError("Name field is required!");
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email field is required!");
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password field is required!");
                    return;
                } else if (password.length() < 6) {
                    mPassword.setError("Password must be more than 6 " +
                            "characters!");
                    return;
                } else if (TextUtils.isEmpty(confirmPass)) {
                    mConfirmPassword.setError("Confirm password field is " +
                            "required!");
                    return;
                } else if (!password.equals(confirmPass)) {
                    mConfirmPassword.setError("Password is not match!");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Account " +
                                            "created",
                                    Toast.LENGTH_SHORT).show();

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

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),
                SignInActivity.class));
    }
}