/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.booklister;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * An {@link BookAdapter} knows how to create a list item layout for each book
 * in the data source (a list of {@link Book} objects).
 *
 * These list item layouts will be provided to an adapter view like GridView
 * to be displayed to the user.
 */
public class BookAdapter extends ArrayAdapter<Book> {

    private static final String LOG_TAG = BookAdapter.class.getName();

    /**
     * Constructs a new {@link BookAdapter}.
     *
     * @param context of the app
     * @param books is the list of books, which is the data source of the adapter
     */
    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    /**
     * Returns a grid item view that displays information about the book at the given position
     * in the list of books.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing grid item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View gridItemView = convertView;
        if (gridItemView == null) {
            gridItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.books_list_item, parent, false);
        }

        // Find the book at the given position in the list of books
        Book currentBook = getItem(position);

        // Find the TextView with view ID book_title
        TextView titleView = (TextView) gridItemView.findViewById(R.id.book_title);
        // Fetch the title from the url.
        String bookTitle = currentBook.getTitle();
        // Display the title of the current book in that TextView
        titleView.setText(bookTitle);

        // Find the TextView with view ID book_author
        TextView authorView = (TextView) gridItemView.findViewById(R.id.book_author);
        // Fetch the author from the url.
        String bookAuthor = currentBook.getAuthor();
        // Display the title of the current book in that TextView
        authorView.setText(bookAuthor);

        // Find the RatingBar with view ID rating_bar
        RatingBar ratingBar = (RatingBar) gridItemView.findViewById(R.id.rating_bar);
        // Fetch the rating from the url.
        float rating = (float) currentBook.getRating();
        // Set the rating of the current book in that RatingBar
        ratingBar.setRating(rating);

        // Find the ImageView with view ID book_thumbnail
        ImageView bookThumbnail = (ImageView) gridItemView.findViewById(R.id.book_thumbnail);
        // Fetch the thumbnail from the url.
        String imageBitmapURL = currentBook.getImageThumbnailURL();
        // Set the thumbnail of the current book in that ImageView
        if(imageBitmapURL != null) {
            Picasso.with(getContext()).load(imageBitmapURL).into(bookThumbnail);
        } else {
            bookThumbnail.setImageResource(R.drawable.cover_temp);
        }

        // Set an item click listener on the GridView, which sends an intent to a web browser
        // to open a website with more information about the selected book.
        View book_layout = gridItemView.findViewById(R.id.book_layout);
        final String url = currentBook.getPreviewURL();
        book_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                getContext().startActivity(intent);
            }
        });

        // Return the grid item view that is now showing the appropriate data
        return gridItemView;
    }
}
