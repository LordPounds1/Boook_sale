package com.example.boook_sale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookDetailActivity extends AppCompatActivity {
    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublisher;
    private TextView tvPageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        ivBookCover = findViewById(R.id.ivBookCover);
        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvPublisher = findViewById(R.id.tvPublisher);
        tvPageCount = findViewById(R.id.tvPageCount);

        // Получение объекта Book из интента
        Book book = (Book) getIntent().getSerializableExtra("BOOK_DETAIL");

        if (book != null) {
            tvTitle.setText(book.getTitle());
            tvAuthor.setText(book.getAuthor());
            // Здесь добавьте другие поля
        }

        Button addBookButton = findViewById(R.id.addToMyBooks);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book newBook = new Book();
                newBook.setTitle(tvTitle.getText().toString());
                newBook.setAuthor(tvAuthor.getText().toString());
                addBookToFirestore(newBook);
            }
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
