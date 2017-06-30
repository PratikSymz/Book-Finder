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

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = BookActivity.class.getName();

    /**
     * URL for book data from the Google Books dataset
     */
    private static final String GOOGLE_API_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if we're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    // LayoutManager for defining the grid of books
    private GridLayoutManager gridLayoutManager;

    // Adapter for the list of books
    private RecyclerViewAdapter mAdapter;

    // TextView that is displayed when the list is empty
    @BindView(R.id.empty_view)
    TextView mEmptyStateTextView;


    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    // SearchView for entering user query.
    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    // String representing the user entered query
    public String query = "";

    // String query to be searched
    public String searchQuery = "";

    // Boolean representing if searching of books is taking place or not.
    public boolean searching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_main_activity);
        ButterKnife.bind(this);

        // Setup a grid depending on screen size
        int screenWidth = getResources().getConfiguration().screenWidthDp;
        if (screenWidth <= 360) {
            //j5 portrait
            gridLayoutManager = new GridLayoutManager(BookActivity.this, 2);
        } else if (screenWidth > 360 && screenWidth <= 600) {
            //asus portrait
            gridLayoutManager = new GridLayoutManager(BookActivity.this, 3);
        } else if (screenWidth > 600 && screenWidth <= 800) {
            //j5 landscape
            gridLayoutManager = new GridLayoutManager(BookActivity.this, 4);
        } else if (screenWidth > 800) {
            //asus landscape
            gridLayoutManager = new GridLayoutManager(BookActivity.this, 5);
        }


        // Setup a recyclerView to set the gridLayout
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Setup an animator on the recyclerView
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(1000);
        animator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(animator);

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new RecyclerViewAdapter(this, new ArrayList<Book>());
        recyclerView.setAdapter(mAdapter);

        final View loadingIndicator = findViewById(R.id.progressBar);

        // Setting the layout with an empty text view
        // mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        // bookGridView.setEmptyView(mEmptyStateTextView);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        recyclerView.setAdapter(mAdapter);

        // If there is a network connection, fetch data
        if (isOnline()) {
            // Default query for searching
            query = GOOGLE_API_URL + "android&orderBy=newest&maxResults=40";

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this).forceLoad();
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        // Retrieve the user entered search query.
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search only if there is network connectivity
                if (isOnline()) {
                    searching = true;
                    loadingIndicator.setVisibility(View.VISIBLE);
                    String clientQuery = searchView.getQuery().toString();
                    clientQuery = clientQuery.replace(" ", "+");
                    searchQuery = GOOGLE_API_URL + clientQuery.trim() +
                            "&orderBy=relevance&maxResults=40&";

                    getLoaderManager().restartLoader(BOOK_LOADER_ID, null, BookActivity.this);
                    searchView.clearFocus();

                    // Clear the view of previous book data
                    recyclerView.getRecycledViewPool().clear();
                } else {
                    loadingIndicator.setVisibility(View.GONE);
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    mEmptyStateTextView.setText("Network not available");

                    // Clear the view of previous book data
                    recyclerView.getRecycledViewPool().clear();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Floating action button used for returning to the default home page by
        // resetting the searchQuery
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform search only if there is network connectivity
                if (isOnline()) {
                    searching = true;
                    loadingIndicator.setVisibility(View.VISIBLE);
                    searchQuery = query;
                    getLoaderManager().restartLoader(BOOK_LOADER_ID, null, BookActivity.this);
                    searchView.clearFocus();

                    // Clear the view of previous book data
                    recyclerView.getRecycledViewPool().clear();
                } else {
                    loadingIndicator.setVisibility(View.GONE);
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    mEmptyStateTextView.setText("Network not available");

                    // Clear the view of previous book data
                    recyclerView.getRecycledViewPool().clear();
                }
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        if (searching) {
            return new BookLoader(this, searchQuery);
        }
        return new BookLoader(this, query);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.progressBar);
        loadingIndicator.setVisibility(View.GONE);

        // searchView.setVisibility(View.VISIBLE);

        // Clear the view of previous book data
        recyclerView.getRecycledViewPool().clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the GridView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
            recyclerView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            // Set empty state text to display "No books found."
            mEmptyStateTextView.setText(R.string.no_books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        recyclerView.getRecycledViewPool().clear();
    }

    // Helper function to check the network connectivity
    public boolean isOnline() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}

