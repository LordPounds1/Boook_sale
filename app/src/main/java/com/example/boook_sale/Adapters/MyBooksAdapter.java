package com.example.boook_sale.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.boook_sale.DB.Book;
import com.example.boook_sale.R;
import java.util.List;

public class MyBooksAdapter extends RecyclerView.Adapter<MyBooksAdapter.ViewHolder> {

    private List<Book> books;
    private OnItemClickListener listener;
    private View.OnClickListener mOnClickListener;

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvAuthor;
        public ImageView ivBookCover;

        public ViewHolder(View itemView, OnItemClickListener listener, List<Book> books) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            ivBookCover = itemView.findViewById(R.id.ivBookCover);
            itemView.setOnClickListener(v -> listener.onItemClick(books.get(getAdapterPosition())));
        }
    }

    public MyBooksAdapter(List<Book> books, OnItemClickListener listener) {
        this.books = books;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view, listener, books);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = books.get(position);
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        if (book.getLargeCoverUrl() != null && !book.getLargeCoverUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(book.getLargeCoverUrl())
                    .placeholder(R.drawable.ic_nocover)
                    .into(holder.ivBookCover);
        } else {
            holder.ivBookCover.setImageResource(R.drawable.ic_nocover);
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void updateBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }
}



