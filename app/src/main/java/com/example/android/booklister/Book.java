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

/**
 * An {@link Book} object contains information related to a single book.
 */
public class Book {

    /** Title of the book */
    private String mTitle;

    /** Author of the book */
    private String mAuthor;

    /** Rating of the book */
    private double mRating;

    /** Thumbnail URL of the book */
    private String mImageThumbnailURL;

    /** Preview URL of the book */
    private String mPreviewURL;

    /**
     * Constructs a new {@link Book} object.
     *
     * @param title is the title of the book
     * @param author is the author of the book
     * @param rating is the rating of the book
     * @param imageThumbnailURL is the image URL of the book
     * @param previewURL is the preview URL of the book
     */
    public Book(String title, String author, double rating,
                String imageThumbnailURL, String previewURL) {
        mTitle = title;
        mAuthor = author;
        mRating = rating;
        mImageThumbnailURL = imageThumbnailURL;
        mPreviewURL = previewURL;
    }

    /**
     * Returns the title of the book.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the author of the book.
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Returns the rating of the book.
     */
    public double getRating() {
        return mRating;
    }



    /**
     * Returns the Photo Thumbnail URL of the book.
     */
    public String getImageThumbnailURL() {
        return mImageThumbnailURL;
    }

    /**
     * Returns the Preview URL of the book.
     */
    public String getPreviewURL() {
        return mPreviewURL;
    }
}
