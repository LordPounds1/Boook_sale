package com.example.boook_sale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        Button loginButton = findViewById(R.id.buttonLogin);  // Make sure this ID matches the one in XML.

        // Проверка статуса пользователя и обновление интерфейса
        updateUI(mAuth.getCurrentUser());

        loginButton.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                // Если пользователь уже вошел в систему, выполняем выход
                mAuth.signOut();
                updateUI(null);  // Обновляем UI после выхода
            } else {
                // Если пользователь не вошел, переходим к странице аутентификации
                Intent intent = new Intent(this, AuthenticationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String email, String password) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Вход выполнен успешно, обновляем UI с информацией о пользователе
                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Если вход не удался, отображаем сообщение пользователю
                            Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_LONG).show();
        }
    }


    private void updateUI(FirebaseUser user) {
        TextView userEmail = findViewById(R.id.textViewUserEmail);
        Button loginButton = findViewById(R.id.buttonLogin);

        if (user != null) {
            // Пользователь вошел в систему
            userEmail.setText(user.getEmail());
            userEmail.setVisibility(View.VISIBLE);
            loginButton.setText("LOG OUT");
        } else {
            // Пользователь не вошел в систему
            userEmail.setVisibility(View.GONE);
            loginButton.setText("LOG IN");
        }
    }
}


