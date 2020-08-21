package com.example.games4you.logic.YouTubeAPI;

import android.os.AsyncTask;
import android.util.Log;

import com.example.games4you.GamePageFragment;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeSearchAPI extends AsyncTask<Void, Void, Void> {


    private  List<SearchResult> searchResultList;
    private boolean isReady;
    private String keywords;
    private GamePageFragment.IProcess mProcess;
    public YoutubeSearchAPI(String keywords, GamePageFragment.IProcess mProcess) {
        this.keywords = keywords;
        this.mProcess=mProcess;
    }

    @Override
    protected Void doInBackground(Void... params) {
        isReady = false;
        try {

            YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("VideoStoreAdmin").build();
            searchResultList = new ArrayList<>();
            YouTube.Search.List search = youtube.search().list("id,snippet");
            search.setKey("AIzaSyDE-HFlnjofWDZ2Er2dPwQmfvXi4vxBmmU");
            search.setQ(keywords);
            search.setType("video");
            search.setMaxResults(1l);


            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
           searchResultList = searchResponse.getItems();

            if (searchResultList != null) {
               // Log.e("YouTube",searchResultList.iterator().next().getId().getVideoId());
                isReady=true;
            }

        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        //Do All UI Changes HERE
        super.onPostExecute(aVoid);
        mProcess.updateAdapter();
    }
    public List<String> getVideoId()

    {
        List<String> ans = new ArrayList<>();
        ans.add(searchResultList.iterator().next().getId().getVideoId());
        return ans;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
