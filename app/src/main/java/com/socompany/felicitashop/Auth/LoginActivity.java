package com.socompany.felicitashop.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.socompany.felicitashop.MainActivities.MainActivity;
import com.socompany.felicitashop.Prevalent.Prevalent;
import com.socompany.felicitashop.R;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private TextView signUpText;
    private FirebaseAuth fireAuth;
    private EditText emailInput, passwordInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Paper.init(this);

        loginUser();

        initialize();

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Вхід...");
                progressDialog.setMessage("Будь ласка, зачекайте");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                String userEmail = emailInput.getText().toString().trim();
                String userPassword = passwordInput.getText().toString();

                if(!userEmail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    if(!userPassword.isEmpty()) {
                        fireAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                saveUserDataIntoPaper(userEmail, userPassword);
                                progressDialog.dismiss();
                                startActivity(mainIntent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Can add exception parser her
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Введіть пароль", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Введіть дійсну пошту", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser() {
        try {
            String userEmail = Paper.book().read(Prevalent.userEmailKey);
            String userPassword = Paper.book().read(Prevalent.userPasswordKey);

            if(userEmail != null) {
                if(userPassword != null) {

                    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        } catch (Exception e) {
            //do nothing
        }
    }

    private void saveUserDataIntoPaper(String userEmail, String userPassword) {
        FirebaseApp.initializeApp(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");

        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String curEmail = userSnapshot.child("Email").getValue(String.class);
                    if(curEmail.equals(userEmail)) {
                        String userName = userSnapshot.child("Name").getValue(String.class);
                        String userPhone = userSnapshot.child("Phone").getValue(String.class);
                        writeUserData(userPhone, userName, userEmail, userPassword);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Упс, щось пішло не так", Toast.LENGTH_SHORT).show();
            }
        };

        usersRef.addListenerForSingleValueEvent(vel);
    }

    private void writeUserData(String phone, String name, String userEmail, String password) {
        Paper.book().write(Prevalent.userPhoneKey, phone);
        Paper.book().write(Prevalent.userNameKey, name);
        Paper.book().write(Prevalent.userEmailKey, userEmail);
        Paper.book().write(Prevalent.userPasswordKey, password);
    }

    private void initialize() {
        fireAuth = FirebaseAuth.getInstance();
        signUpText = findViewById(R.id.login_to_signup);
        emailInput = findViewById(R.id.login_email);
        passwordInput = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
    }
}