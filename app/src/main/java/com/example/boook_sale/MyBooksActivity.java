package com.example.boook_sale;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.boook_sale.Adapters.MyBooksAdapter;
import com.example.boook_sale.DB.Book;
import com.example.boook_sale.Users_auth.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashSet;

public class MyBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyBooksAdapter adapter;
    private HashSet<Book> myBooksSet = new HashSet<>(); // Используем HashSet для исключения повторений

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        recyclerView = findViewById(R.id.recyclerViewMyBooks);
        adapter = new MyBooksAdapter(new ArrayList<>(myBooksSet), this::onBookClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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

        loadMyBooks();
    }

    private void loadMyBooks() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore.getInstance().collection("Users").document(user.getUid()).collection("MyBooks")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Book book = document.toObject(Book.class);
                            myBooksSet.add(book); // Добавляем книгу в HashSet
                        }
                        adapter.updateBooks(new ArrayList<>(myBooksSet)); // Обновляем адаптер новым списком
                    })
                    .addOnFailureListener(e -> Toast.makeText(MyBooksActivity.this, "Error loading books: " + e.getMessage(), Toast.LENGTH_LONG).show());
        } else {
            Toast.makeText(this, "You need to be logged in to view your books", Toast.LENGTH_LONG).show();
        }
    }

    private void onBookClick(Book book) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra(BookDetailActivity.BOOK_DETAIL_KEY, book);
        startActivity(intent);
    }
}






