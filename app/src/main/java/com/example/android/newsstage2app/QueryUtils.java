package com.example.android.newsstage2app;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> newsList = extractFeatureFromJson(jsonResponse);
        return newsList;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        List<News> newsList = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject baseJsonResults = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = baseJsonResults.getJSONArray("results");
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNews = newsArray.getJSONObject(i);
                String sectionName = currentNews.getString("sectionName");
                String webTitle = currentNews.getString("webTitle");
                String webPublicationDate = currentNews.getString("webPublicationDate");
                String webUrl = currentNews.getString("webUrl");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                try {
                    Date newDate = format.parse(webPublicationDate);
                    format = new SimpleDateFormat("dd-MM-yyyy" + "\n" + "HH:mm:ss");
                    webPublicationDate = format.format(newDate);
                } catch (ParseException e) {
                    Log.e(LOG_TAG, "Problem with parsing the date format");
                }
                String author = null;
                if (currentNews.has("tags")) {
                    // Extract the JSONArray associated with the key called "tags"
                    JSONArray tagsArray = currentNews.getJSONArray("tags");
                    if (tagsArray.length() != 0) {
                        // Extract the first JSONObject in the tagsArray
                        JSONObject firstTagsItem = tagsArray.getJSONObject(0);
                        // Extract the value for the key called "webTitle"
                        author = firstTagsItem.getString("webTitle");
                    }
                }
                News news = new News(sectionName, author, webTitle, "Date:" + "\n" + webPublicationDate, webUrl);
                newsList.add(news);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }
        return newsList;
    }
}
