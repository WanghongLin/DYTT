/*
 * Copyright (C) 2016 wanghong
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

package com.wanghong.dytt;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;


public class DyttListFragment extends Fragment {

    private static final String TAG = DyttListFragment.class.getSimpleName();

    private static final String ARG_URL = "url";
    private static final String ARG_TITLE = "title";
    private static final String ARG_TYPE = "type";

    private String url;
    private String title;
    private int type;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private DyttListAdapter dyttListAdapter;
    private static final int STARTED_PAGE = 1;

    public DyttListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param url url of the category
     * @param title title in tab
     * @param type
     * @return A new instance of fragment DyttListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DyttListFragment newInstance(String url, String title, int type) {
        DyttListFragment fragment = new DyttListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(ARG_URL);
            title = getArguments().getString(ARG_TITLE);
            type = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dytt_list, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.dytt_list_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dyttListAdapter = null;
                loadNextDataFromApi(0, true);
            }
        });
        progressBar = (ProgressBar) view.findViewById(R.id.dytt_list_progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.dytt_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page, false);
            }
        });
        return view;
    }

    private void loadNextDataFromApi(int page, boolean swipeRefresh) {
        String url = String.format(this.url, page+STARTED_PAGE);
        Log.d(TAG, "loadNextDataFromApi: " + url);
        if (progressBar != null && !swipeRefresh) {
            progressBar.setVisibility(View.VISIBLE);
        }
        new JsoupEngine<DyttListItem>().setPageUrl(url).setResultClass(DyttListItem.class)
                .setJsoupEngineCallback(new JsoupEngine.JsoupEngineCallback<DyttListItem>() {
                    @Override
                    public void onJsoupParsed(List<DyttListItem> results) {
                        if (results != null) {
                            for (DyttListItem result : results) {
                                result.setType(type);
                            }
                            if (swipeRefreshLayout != null) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            if (progressBar != null) {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            if (dyttListAdapter == null) {
                                dyttListAdapter = new DyttListAdapter(results, getActivity());
                                recyclerView.setAdapter(dyttListAdapter);
                            } else {
                                dyttListAdapter.handleMoreData(results);
                            }
                        }
                    }
                }).parseAsync();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        dyttListAdapter = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadNextDataFromApi(0, false);
    }

    public String getTitle() {
        return title;
    }
}
