package com.socompany.felicitashop.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.socompany.felicitashop.Prevalent.Prevalent;
import com.socompany.felicitashop.R;

import io.paperdb.Paper;

public class SignUpActivity extends AppCompatActivity {

    private TextView loginReturnText;
    private FirebaseAuth fireAuth;
    private EditText emailInput, passwordInput;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialize();

        loginReturnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(LoginIntent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = emailInput.getText().toString().trim();
                String userPassword = passwordInput.getText().toString();

                if(userEmail.isEmpty() && userPassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Введіть усі поля", Toast.LENGTH_SHORT).show();
                } else {
                    fireAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                saveUserInfo(userEmail, userPassword);

                                startActivity(new Intent(SignUpActivity.this, SignUp2Page.class));
                            } else {
                                Toast.makeText(SignUpActivity.this, "Error: " + task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void saveUserInfo(String userEmail, String userPassword) {
        Paper.book().write(Prevalent.userEmailKey, userEmail);
        Paper.book().write(Prevalent.userPasswordKey, userPassword);
    }

    private void initialize() {
        fireAuth = FirebaseAuth.getInstance();
        loginReturnText = findViewById(R.id.signup_to_login);
        emailInput = findViewById(R.id.signup_email);
        passwordInput = findViewById(R.id.signup_password);
        signUpButton = findViewById(R.id.signup_button);
    }
}