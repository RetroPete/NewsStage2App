package com.example.android.newsstage2app;

public class News {

    private String mSectionName;

    private String mAuthor;

    private String mWebTitle;

    private String mDate;

    private String mUrl;

    public News(String sectionName, String author, String webTitle, String date, String url) {
        mSectionName = sectionName;
        mAuthor = author;
        mWebTitle = webTitle;
        mDate = date;
        mUrl = url;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getAuthor(){
        return mAuthor;
    }

    public String getWebTitle() {
        return mWebTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }
}
