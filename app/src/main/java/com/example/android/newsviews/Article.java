package com.example.android.newsviews;

public class Article {
    private static final int NO_IMAGE_PROVIDED = -1;
    private  String mArticleUrl;
    private String mArticleTitle;
    private String mArticleAuthor;
    private int mImageResourceID = NO_IMAGE_PROVIDED;


    /**
     * Create a new Article object.
     *
     * @param author is the author of the Article
     * @param articleUrl is the link to the article
     * @param articleTitle  is the Article title
     */
    public Article(String author, String articleTitle, String articleUrl) {
        mArticleTitle = articleTitle;
        mArticleAuthor = author;
        mArticleUrl = articleUrl;
    }

    /**
     * Create a new Article object with an associated image
     *
     * @param author is the author of the Article
     * @param articleTitle  is the Article title
     * @param articleUrl is the link to the article
     * @param imageID    is the image resource ID associated with the Article.
     */
    public Article(String author, String articleTitle, String articleUrl, int imageID) {
        mArticleTitle = articleTitle;
        mArticleAuthor = author;
        mArticleUrl = articleUrl;
        mImageResourceID = imageID;
    }

    public String getArticleTitle() {
        return mArticleTitle;
    }

    /**
     * @return id for the image resource for this Article
     */
    public String getArticleAuthor() {
        return mArticleAuthor;
    }

    public boolean hasImage() {
        return mImageResourceID != NO_IMAGE_PROVIDED;
    }

    public int getImageResourceID() {
        return mImageResourceID;
    }


    @Override
    public String toString() {
        return "Article{" +
                "mArticleTitle='" + mArticleTitle + '\'' +
                ", mArticleAuthor='" + mArticleAuthor + '\'' +
                ", mImageResourceID=" + mImageResourceID +
                '}';
    }
}