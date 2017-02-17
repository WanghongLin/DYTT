/*
 * Copyright (C) 2017 wanghong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wanghong.dytt.imdb;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.wanghong.dytt.ActivityConstants;
import com.wanghong.dytt.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wanghong on 2/17/17.
 */

public class IMDBListFragment extends Fragment {

    private static final String IMDB_LIST_URL_EXTRA = "imdb_list_url_extra";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    public IMDBListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_imdb_list, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.imdb_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        progressBar = (ProgressBar) rootView.findViewById(R.id.imdb_list_progressbar);
        final String url = getArguments().getString(IMDB_LIST_URL_EXTRA);
        new IMDBListTask(true).execute(url);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.imdb_list_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new IMDBListTask(false).execute(url);
            }
        });

        return rootView;
    }

    public static IMDBListFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(IMDB_LIST_URL_EXTRA, url);
        IMDBListFragment fragment = new IMDBListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private class IMDBListTask extends AsyncTask<String, Void, List<IMDBListItem>> {

        private boolean showProgressBar;
        private final Pattern TT_PATTERN = Pattern.compile(".*/(tt\\d+)/.*");

        public IMDBListTask(boolean showProgressBar) {
            this.showProgressBar = showProgressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressBar != null && showProgressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected List<IMDBListItem> doInBackground(String... params) {

            List<IMDBListItem> imdbListItems = new ArrayList<>();

            try {
                Document document = Jsoup.connect(params[0]).userAgent(ActivityConstants.USER_AGENT).get();
                Elements posterElements = document.select("tr > td.posterColumn > a[href] > img");
                Elements titleElements = document.select("tr > td.titleColumn > a[href]");
                Elements ratingElements = document.select("tr > td.imdbRating > *");

                int size = Math.min(Math.min(posterElements.size(), titleElements.size()), ratingElements.size());

                for (int i = 0; i < size; i++) {
                    IMDBListItem imdbListItem = new IMDBListItem();
                    imdbListItem.setPosterUrl(posterElements.get(i).attr("src"));
                    imdbListItem.setTitle((i+1) + "." + titleElements.get(i).text());
                    imdbListItem.setRating(ratingElements.get(i).text());
                    String href = titleElements.get(i).attr("href");
                    Matcher matcher = TT_PATTERN.matcher(href);
                    if (matcher.matches()) {
                        imdbListItem.setImdbID(matcher.group(1));
                    }
                    imdbListItem.setImdbLink("http://www.imdb.com" + href);
                    imdbListItems.add(imdbListItem);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return imdbListItems;
        }

        @Override
        protected void onPostExecute(List<IMDBListItem> imdbListItems) {
            super.onPostExecute(imdbListItems);
            if (recyclerView != null) {
                recyclerView.setAdapter(new IMDBListAdapter(imdbListItems));
            }
            swipeRefreshLayout.setRefreshing(false);
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}
