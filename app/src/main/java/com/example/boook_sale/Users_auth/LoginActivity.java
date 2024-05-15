package com.example.boook_sale.Users_auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.boook_sale.MainActivity;
import com.example.boook_sale.MyBooksActivity;
import com.example.boook_sale.LoginInformationActivity;
import com.example.boook_sale.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LoginActivity extends Activity {
    private FirebaseAuth mAuth;
    private ImageView userImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        userImageView = findViewById(R.id.imageViewProfile); // Убедитесь, что у вас есть ImageView с таким ID в вашем layout
        Button loginButton = findViewById(R.id.buttonLogin);
        Button buttonNotifications = findViewById(R.id.buttonNotifications);

        updateUI(mAuth.getCurrentUser());

        buttonNotifications.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, LoginInformationActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                mAuth.signOut();
                updateUI(null);
            } else {
                Intent intent = new Intent(this, AuthenticationActivity.class);
                startActivity(intent);
            }
        });

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
    }

    private void updateUI(FirebaseUser user) {
        TextView userDetailTextView = findViewById(R.id.textViewUserName);
        ImageView userImageView = findViewById(R.id.imageViewProfile); // Ensure this is the correct ImageView ID
        Button loginButton = findViewById(R.id.buttonLogin);

        if (user != null) {
            String displayName = user.getDisplayName();
            String email = user.getEmail();
            String phoneNumber = user.getPhoneNumber();

            // Prioritize the user info display: 1. Display Name, 2. Email, 3. Phone Number
            if (displayName != null && !displayName.isEmpty()) {
                userDetailTextView.setText(displayName);
            } else if (email != null && !email.isEmpty()) {
                userDetailTextView.setText(email);
            } else if (phoneNumber != null && !phoneNumber.isEmpty()) {
                userDetailTextView.setText(phoneNumber);
            }

            userDetailTextView.setVisibility(View.VISIBLE);
            loginButton.setText("LOG OUT");
            loadImage(user.getUid());

            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl()).into(userImageView);
                userImageView.setVisibility(View.VISIBLE);
            } else {
                userImageView.setVisibility(View.GONE);  // Or set a default image
            }
        } else {
            userDetailTextView.setVisibility(View.GONE);
            userImageView.setVisibility(View.GONE);
            loginButton.setText("LOG IN");
        }
    }



    private void loadImage(String userId) {
        // Создаем экземпляр FirebaseStorage с указанием URL бакета
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://booksale-7af20.appspot.com");

        // Получаем ссылку на изображение пользователя в директории 'users_image'
        StorageReference storageReference = storage.getReference().child("users_image/" + userId + ".jpg");

        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            // Используем Glide для загрузки изображения
            Glide.with(LoginActivity.this)
                    .load(uri)
                    .into(userImageView);
            userImageView.setVisibility(View.VISIBLE);
        }).addOnFailureListener(e -> {
            // Если не удалось загрузить изображение, устанавливаем изображение по умолчанию
            userImageView.setImageResource(R.drawable.user_847969);  // Убедитесь, что default_icon есть в вашем ресурсе drawable
            userImageView.setVisibility(View.VISIBLE);
        });
    }
}



