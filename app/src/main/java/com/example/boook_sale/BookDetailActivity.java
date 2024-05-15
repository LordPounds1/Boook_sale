package com.example.boook_sale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.boook_sale.DB.Book;
import com.example.boook_sale.Users_auth.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class BookDetailActivity extends AppCompatActivity {
    private static final String TAG = "BookDetailActivity";

    public static final String BOOK_DETAIL_KEY = "book";
    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        ivBookCover = findViewById(R.id.ivBookCover);
        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);



        Book book = (Book) getIntent().getSerializableExtra(BOOK_DETAIL_KEY);
        if (book != null) {
            Log.d(TAG, "Received book: " + book.getTitle());
            if (book.getLargeCoverUrl() != null && !book.getLargeCoverUrl().isEmpty()) {
                Log.d(TAG, "Loading image from URL: " + book.getLargeCoverUrl());
                Glide.with(this) // Context
                        .load(book.getLargeCoverUrl()) // URL или URI
                        .placeholder(R.drawable.ic_nocover) // Заглушка, пока изображение загружается
                        .error(R.drawable.ic_nocover) // Изображение в случае ошибки
                        .into(ivBookCover);
            } else {
                Log.w(TAG, "No cover URL available, setting default image.");
                ivBookCover.setImageResource(R.drawable.ic_nocover);
            }
            tvTitle.setText(book.getTitle());
            tvAuthor.setText(book.getAuthor());
        } else {
            Log.e(TAG, "Book data is null.");
        }

        Button addBookButton = findViewById(R.id.addToMyBooks);
        addBookButton.setOnClickListener(view -> {
            if (book != null) {
                addBookToFirestore(book);
            } else {
                Toast.makeText(BookDetailActivity.this, "Error: Book details not available", Toast.LENGTH_SHORT).show();
            }
        });

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
    }

    public void addBookToFirestore(Book book) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && book != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(user.getUid()).collection("MyBooks")
                    .add(book)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(BookDetailActivity.this, "Book added successfully!", Toast.LENGTH_SHORT).show();
                        // Удалите Intent, если не хотите сразу переходить в MyBooksActivity
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(BookDetailActivity.this, "Error adding book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "You need to be logged in to add books", Toast.LENGTH_LONG).show();
        }
    }
}
