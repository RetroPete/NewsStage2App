package com.example.android.newsstage2app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> newsList) {
        super(context, 0, newsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (convertView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }
        News currentNews = getItem(position);
        TextView sectionNameView = (TextView) listItemView.findViewById(R.id.sectionName);
        sectionNameView.setText(currentNews.getSectionName());

        TextView webTitleView = (TextView) listItemView.findViewById(R.id.webTitle);
        webTitleView.setText(currentNews.getWebTitle());

        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        authorView.setText(currentNews.getAuthor());

        TextView webPublicationDateView = (TextView) listItemView.findViewById(R.id.webPublicationDate);
        webPublicationDateView.setText(currentNews.getDate());
        return listItemView;
    }
}