package com.example.boook_sale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.boook_sale.Users_auth.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginInformationActivity extends AppCompatActivity {

    private TextView textViewLoginMethod, textViewUserEmail, textViewUserName, textViewUserPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_information);

        textViewLoginMethod = findViewById(R.id.textViewLoginMethod);
        textViewUserEmail = findViewById(R.id.textViewUserEmail);
        textViewUserPhone = findViewById(R.id.textViewUserPhone);

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

        updateUI(FirebaseAuth.getInstance().getCurrentUser());
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            String email = user.getEmail(); // Электронная почта пользователя
            String phone = user.getPhoneNumber(); // Телефон пользователя

            // Устанавливаем имя и фамилию

            // Проверяем метод входа
            if (phone != null) {
                // Вход выполнен через телефон
                textViewLoginMethod.setText("Login Method: Phone");
                textViewUserPhone.setText("Phone: " + phone);
                textViewUserEmail.setVisibility(View.GONE); // Скрываем поле email
                textViewUserPhone.setVisibility(View.VISIBLE); // Показываем поле телефона
            } else if (email != null) {
                // Вход выполнен через email
                textViewLoginMethod.setText("Login Method: Email");
                textViewUserEmail.setText("Email: " + email);
                textViewUserEmail.setVisibility(View.VISIBLE); // Показываем поле email
                textViewUserPhone.setVisibility(View.GONE); // Скрываем поле телефона
            } else {
                // Неопределенный метод входа
                textViewLoginMethod.setText("Login Method: Unknown");
                textViewUserEmail.setVisibility(View.GONE);
                textViewUserPhone.setVisibility(View.GONE);
            }
        } else {
            // Пользователь не в системе
            textViewUserName.setText("User not logged in");
            textViewUserEmail.setVisibility(View.GONE);
            textViewUserPhone.setVisibility(View.GONE);
            textViewLoginMethod.setText("Login Method: N/A");
        }
    }
}


