package com.socompany.felicitashop.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.socompany.felicitashop.Prevalent.Prevalent;
import com.socompany.felicitashop.R;
import com.socompany.felicitashop.model.Users;

import java.util.HashMap;
import java.util.Objects;

import io.paperdb.Paper;

public class SignUp2Page extends AppCompatActivity {

    private EditText nameInput, numberInput;
    private Button signUpButton;
    private DatabaseReference firebaseDatabase;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2_page);

        Paper.init(this);

        initialize();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String phone = numberInput.getText().toString();

                if(!name.isEmpty() && validatePhone(phone)) {
                    createNewAccount(name, phone);
                }
            }
        });
    }

    private void createNewAccount(String name, String phone) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating account");
        progressDialog.setMessage("Please, wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        String userEmail = Paper.book().read(Prevalent.userEmailKey);
        String userPassword = Paper.book().read(Prevalent.userPasswordKey);

        if(userEmail != null && userPassword != null) {
            validateUser(userEmail, userPassword, name, phone);
        }

    }

    private void validateUser(String userEmail, String userPassword, String name, String phone) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(phone).exists())) {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("Phone", phone);
                    userDataMap.put("Name", name);
                    userDataMap.put("Password", userPassword);
                    userDataMap.put("Email", userEmail);
                    userDataMap.put("IsAdmin", "false");

                    rootRef.child("Users").child(phone).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        saveAllUserInfo(userEmail, userPassword, phone, name);
                                        Toast.makeText(SignUp2Page.this, "Аккаунт створено", Toast.LENGTH_SHORT).show();
                                        Intent loginIntent = new Intent(SignUp2Page.this, LoginActivity.class);
                                        startActivity(loginIntent);
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUp2Page.this, "Щось пішло не так", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(SignUp2Page.this, "Аккаунт з таким номером вже існує", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUp2Page.this, "Помилка: " + error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAllUserInfo(String userEmail, String userPassword, String phone, String name) {
        Paper.book().write(Prevalent.userEmailKey, userEmail);
        Paper.book().write(Prevalent.userPasswordKey, userPassword);
        Paper.book().write(Prevalent.userPhoneKey, phone);
        Paper.book().write(Prevalent.userNameKey, name);
    }

    private boolean validatePhone(String phone) {
        if(phone.isEmpty()) {
            return false;
        } else if(phone.length() != 10) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    private void initialize() {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        nameInput = findViewById(R.id.signup2_name);
        numberInput = findViewById(R.id.signup2_number);
        signUpButton = findViewById(R.id.signup2_button);
    }
}