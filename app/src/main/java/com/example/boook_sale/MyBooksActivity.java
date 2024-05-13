package com.example.boook_sale;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBooksActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<Book> adapter;
    private List<Book> myBooks;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        listView = findViewById(R.id.listViewMyBooks);
        myBooks = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myBooks);
        listView.setAdapter(adapter);

        if (getIntent().getBooleanExtra("updateNeeded", false)) {
            loadMyBooks();  // Загрузка или обновление списка книг
        }
    }

    private void loadMyBooks() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(user.getUid()).collection("MyBooks")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<Book> fetchedBooks = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Book book = document.toObject(Book.class);
                                fetchedBooks.add(book);
                            }
                            // Обновление UI
                            updateBooksList(fetchedBooks);
                        } else {
                            Toast.makeText(this, "Error loading books: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please log in to view your books", Toast.LENGTH_LONG).show();
        }
    }



    private void updateBooksList(List<Book> books) {
        myBooks.clear();
        myBooks.addAll(books);
        adapter.notifyDataSetChanged(); // Убедитесь, что адаптер подключен к ListView или RecyclerView
    }

}




