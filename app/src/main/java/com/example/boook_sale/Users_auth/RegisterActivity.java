package com.example.boook_sale.Users_auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.boook_sale.DB.User;
import com.example.boook_sale.MainActivity;
import com.example.boook_sale.MyBooksActivity;
import com.example.boook_sale.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity"; // Для логирования
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText editTextEmailRegister;
    private EditText editTextPasswordRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        editTextEmailRegister = findViewById(R.id.editTextEmailRegister);
        editTextPasswordRegister = findViewById(R.id.editTextPasswordRegister);

        Button registerButton = findViewById(R.id.buttonRegister);

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_main) {
                startActivity(new Intent(this, MainActivity.class));
            }   else if (itemId == R.id.navigation_my_books) {
                startActivity(new Intent(this, MyBooksActivity.class));
            } else if (itemId == R.id.navigation_login) {
                startActivity(new Intent(this, LoginActivity.class));
            }
            return true;
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmailRegister.getText().toString().trim();
                String password = editTextPasswordRegister.getText().toString().trim();
                Log.d(TAG, "Attempting to register with email: " + email);
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    registerUser(email, password);
                } else {
                    Toast.makeText(RegisterActivity.this, "Email and password must not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.d(TAG, "onCreate completed.");
    }

    private void registerUser(String email, String password) {
        Log.d(TAG, "registerUser started with email: " + email);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Registration successful.");
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            Log.d(TAG, "Registered user ID: " + userId);
                            saveUserDataToFirestore(new User(userId, email));
                        }
                    } else {
                        Log.e(TAG, "Registration failed: " + task.getException().getMessage());
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserDataToFirestore(User user) {
        Log.d(TAG, "Saving user data to Firestore for user ID: " + user.getUserId());
        if (user != null) {
            db.collection("Users_data").document(user.getUserId())
                    .set(user)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "User data saved successfully to Firestore.");
                        Toast.makeText(RegisterActivity.this, "User data saved successfully!", Toast.LENGTH_SHORT).show();

                        // Переход на Activity настройки профиля после успешной регистрации
                        Intent profileSetupIntent = new Intent(RegisterActivity.this, ProfileSetupActivity.class);
                        startActivity(profileSetupIntent);
                        finish(); // Закрываем текущую активность, чтобы пользователь не мог вернуться нажатием кнопки назад
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error saving user data: " + e.getMessage());
                        Toast.makeText(RegisterActivity.this, "Error saving user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.e(TAG, "User object is null, cannot save to Firestore.");
        }
    }
}


