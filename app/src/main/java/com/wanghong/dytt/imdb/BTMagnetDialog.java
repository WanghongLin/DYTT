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

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wanghong.dytt.ActivityConstants;
import com.wanghong.dytt.ActivityUtils;
import com.wanghong.dytt.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghong on 2/17/17.
 */

public class BTMagnetDialog {

    private AlertDialog alertDialog;

    private String imdbID;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    private BTMagnetDialog(Context context, String imdbID) {
        this.imdbID = imdbID;

        View customView = LayoutInflater.from(context).inflate(R.layout.bt_magnet_list, null, false);
        progressBar = (ProgressBar) customView.findViewById(R.id.magnet_list_progressbar);
        recyclerView = (RecyclerView) customView.findViewById(R.id.magnet_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        alertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.download_list)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                })
                .setView(customView)
                .create();
    }

    public static void show(Context context, String imdbID) {
        new BTMagnetDialog(context, imdbID).show();
    }

    private void show() {
        alertDialog.show();
        new BTSearchTask().execute(imdbID);
    }

    private class BTSearchTask extends AsyncTask<String, Void, List<BTMagnet>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected List<BTMagnet> doInBackground(String... params) {
            List<BTMagnet> btMagnets = new ArrayList<>();
            try {
                Document document = Jsoup.connect(ActivityConstants.BT_SEARCH_ENDPOINT)
                        .data("keyboard", params[0]).followRedirects(true).post();
                Elements elements = document.select("div.thumbnail-full > a");
                if (elements != null && elements.size() > 0) {
                    String detailUrl = elements.get(0).absUrl("href");
                    if (detailUrl != null && (URLUtil.isHttpsUrl(detailUrl) || URLUtil.isHttpUrl(detailUrl))) {
                        Document detailDocument = Jsoup.connect(detailUrl).get();
                        Elements downloadElements = detailDocument.select("ul.dlist > li > a[href^=magnet]");
                        for (int i = 0; i < downloadElements.size(); i++) {
                            BTMagnet btMagnet = new BTMagnet(downloadElements.get(i).text(),
                                    downloadElements.get(i).attr("href"));
                            btMagnets.add(btMagnet);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return btMagnets;
        }

        @Override
        protected void onPostExecute(List<BTMagnet> btMagnets) {
            super.onPostExecute(btMagnets);
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            if (btMagnets != null) {
                recyclerView.setAdapter(new BTMagnetAdapter(btMagnets));
            }
        }
    }

    private static class BTMagnetViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        private BTMagnet btMagnet;

        public BTMagnetViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.magnet_item_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtils.navigateToThunder((Activity) v.getContext(), btMagnet.getMagnet());
                }
            });
        }

        public void setBtMagnet(BTMagnet btMagnet) {
            this.btMagnet = btMagnet;
        }

        public void invalidate() {
            textView.setText(btMagnet.getTitle());
        }
    }

    private static class BTMagnetAdapter extends RecyclerView.Adapter<BTMagnetViewHolder> {

        private List<BTMagnet> btMagnets;

        public BTMagnetAdapter(List<BTMagnet> btMagnets) {
            this.btMagnets = btMagnets;
        }

        @Override
        public BTMagnetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BTMagnetViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(BTMagnetViewHolder holder, int position) {
            holder.setBtMagnet(btMagnets.get(position));
            holder.invalidate();
        }

        @Override
        public int getItemCount() {
            return btMagnets.size();
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.magnet_list_item;
        }
    }
}
