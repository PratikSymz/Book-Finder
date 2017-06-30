package com.example.android.booklister;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A {@link RecyclerViewAdapter} knows how to create a recycler item layout for each book
 * in the data source (a list of {@link Book} objects).
 *
 * These recycler item layouts will be provided to an adapter view like RecyclerView
 * to be displayed to the user.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    List<Book> bookList;
    LayoutInflater bookInflater;
    Context context;
    private static final String LOG_TAG = RecyclerViewAdapter.class.getName();

    /**
     * Constructs a new {@link RecyclerViewAdapter}.
     *
     * @param context of the app
     * @param bookList is the list of books, which is the data source of the adapter
     */
    public RecyclerViewAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookInflater = LayoutInflater.from(context);
        this.bookList = bookList;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = bookInflater.inflate(R.layout.books_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        // Find the book at the given position in the list of books
        final Book currentBook = bookList.get(position);

        // Fetch the title from the object and set the title of the current book in that TextView.
        holder.titleView.setText(currentBook.getTitle());

        // Fetch the author from the object and set the author of the current book in that TextView.
        holder.authorView.setText("by " + currentBook.getAuthor());

        // Fetch the rating from the object.
        float rating = (float) currentBook.getRating();
        // Set the rating of the current book in that RatingBar
        holder.ratingBar.setRating(rating);

        // Fetch the thumbnail from the object.
        String imageBitmapURL = currentBook.getImageThumbnailURL();
        // Set the thumbnail of the current book in that ImageView
        if(imageBitmapURL != null && !imageBitmapURL.isEmpty()) {
            Picasso.with(context).load(imageBitmapURL).into(holder.bookThumbnail);
        } else {
            holder.bookThumbnail.setImageResource(R.drawable.cover_temp);
        }

        // Set an item click listener on the RecyclerView, which sends an intent to the appropriate
        // application to open the app and show more information about the selected book.
        holder.book_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentBook.getPreviewURL()));
                context.startActivity(intent);
            }
        });
    }

    public void addAll(List<Book> data) {
        bookList.clear();
        bookList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int items = this.bookList.size();
        return items;
    }

    // The ViewHolder class containing all the views of the layout
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Find the TextView with view ID book_title
        @BindView(R.id.book_title)
        TextView titleView;

        // Find the TextView with view ID book_author
        @BindView(R.id.book_author)
        TextView authorView;

        // Find the ImageView with view ID book_thumbnail
        @BindView(R.id.book_thumbnail)
        ImageView bookThumbnail;

        // Find the RatingBar with view ID rating_bar
        @BindView(R.id.rating_bar)
        RatingBar ratingBar;

        @BindView(R.id.book_layout)
        View book_layout;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
