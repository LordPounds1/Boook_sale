package com.example.boook_sale.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boook_sale.DB.Book;
import com.example.boook_sale.R;
import com.squareup.picasso.Picasso;

public class BookDetailAdapter extends RecyclerView.Adapter<BookDetailAdapter.ViewHolder> {

    private Context context;
    private Book book;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvAuthor;
        public TextView tvPublisher;

        public ViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivBookCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
        }
    }

    public BookDetailAdapter(Context context, Book book) {
        this.context = context;
        this.book = book;
    }


    @NonNull
    @Override
    public BookDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View bookDetailView = inflater.inflate(R.layout.activity_book_detail, parent, false);

        // Return a new holder instance
        return new ViewHolder(bookDetailView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookDetailAdapter.ViewHolder viewHolder, int position) {
        // Set item views based on your views and data model
        viewHolder.tvTitle.setText(book.getTitle());
        viewHolder.tvAuthor.setText(book.getAuthor());
        if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
            Picasso.get().load(Uri.parse(book.getCoverUrl())).error(R.drawable.ic_nocover).into(viewHolder.ivCover);
        } else {
            viewHolder.ivCover.setImageResource(R.drawable.ic_nocover);
        }
    }

    @Override
    public int getItemCount() {
        return 1; // There's only one book detail in this adapter
    }
}
