package com.example.android.newsviews;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {

    int mBackGroundColor;

    public ArticleAdapter(Context context, ArrayList<Article> articles, int backGroundColor) {
        super(context, 0, articles);
        mBackGroundColor = backGroundColor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // create (inflate) a new list item view if one isn't available already to reuse
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_view, parent, false);
        }


        // get the Article at this position
        Article currentArticle = getItem(position);

        // populate the TextViews on this list item View with the current Article's values.
        TextView articleTitleView = (TextView) listItemView.findViewById(R.id.article_title);
        articleTitleView.setText(currentArticle.getArticleTitle());

        TextView articleURLView = (TextView) listItemView.findViewById(R.id.article_link);
        articleURLView.setText(currentArticle.getArticleAuthor());
        Linkify.addLinks(articleURLView, Linkify.WEB_URLS);

        ImageView articleImage = (ImageView) listItemView.findViewById(R.id.image);
        if (currentArticle.hasImage()) {
            // unhide image
            articleImage.setVisibility(View.VISIBLE);
            // set image for this view
            articleImage.setImageResource(currentArticle.getImageResourceID());
        } else {
            // remove imageview from layout for this item
            articleImage.setVisibility(View.GONE);
        }

        // in this list item view, grab the text box container for words/tranlations
        View listBox = listItemView.findViewById(R.id.list_item);

        // change background color for this ArticleAdapter to the right id
        listBox.setBackgroundColor(ContextCompat.getColor(getContext(), mBackGroundColor));

        // return this List ItemView to the ListView for display
        return listItemView;
    }


}
