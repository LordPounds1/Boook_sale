package com.example.boook_sale.Users_auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.boook_sale.MainActivity;
import com.example.boook_sale.MyBooksActivity;
import com.example.boook_sale.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileSetupActivity extends AppCompatActivity {

    private static final String TAG = "ProfileSetupActivity";

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private Button buttonSaveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        buttonSaveProfile = findViewById(R.id.buttonSaveProfile);

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_main) {
                startActivity(new Intent(this, MainActivity.class));
            }  else if (itemId == R.id.navigation_my_books) {
                startActivity(new Intent(this, MyBooksActivity.class));
            } else if (itemId == R.id.navigation_login) {
                startActivity(new Intent(this, LoginActivity.class));
            }
            return true;
        });

        buttonSaveProfile.setOnClickListener(v -> saveUserProfile());
    }

    private void saveUserProfile() {
        Log.d(TAG, "saveUserProfile: Saving user profile...");
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "saveUserProfile: Empty fields detected. Profile not saved.");
            return;
        }

        Map<String, Object> user = new HashMap<>();
        user.put("firstName", firstName);
        user.put("lastName", lastName);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Profile_data").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ProfileSetupActivity.this, "Profile Saved Successfully", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "saveUserProfile: Profile saved successfully.");
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "saveUserProfile: Error saving profile", e);
                    Toast.makeText(ProfileSetupActivity.this, "Error saving profile", Toast.LENGTH_SHORT).show();
                });
    }
}




