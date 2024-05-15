package com.example.boook_sale.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.boook_sale.DB.Book;
import com.example.boook_sale.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RandomBookAdapter extends RecyclerView.Adapter<RandomBookAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Book> books;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvAuthor;

        public ViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivBookCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
        }
    }

    public RandomBookAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }


    @Override
    public RandomBookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View bookView = inflater.inflate(R.layout.item_book, parent, false);

        // Return a new holder instance
        return new ViewHolder(bookView);
    }

    @Override
    public void onBindViewHolder(RandomBookAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Book book = books.get(position);

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
        return books.size();
    }
}

