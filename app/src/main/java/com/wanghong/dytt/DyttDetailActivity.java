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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DyttDetailActivity extends AppCompatActivity {

    private static final String TAG = DyttDetailActivity.class.getSimpleName();
    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_TYPE = "extra_type";

    public static final int TYPE_MOVIE = 710;
    public static final int TYPE_DRAMA = 265;

    private ImageView posterImageView;
    private ImageView thumbnailImageView;
    private TextView descriptionTextView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dytt_detail);

        posterImageView = (ImageView) findViewById(R.id.dytt_detail_poster);
        thumbnailImageView = (ImageView) findViewById(R.id.dytt_detail_thumbnail);
        descriptionTextView = (TextView) findViewById(R.id.dytt_detail_description);
        recyclerView = (RecyclerView) findViewById(R.id.dytt_detail_download_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String url = getIntent().getStringExtra(EXTRA_URL);
        int type = getIntent().getIntExtra(EXTRA_TYPE, TYPE_MOVIE);
        if (url != null) {
            if (type == TYPE_DRAMA) {
                new JsoupEngine<DyttTVDramaItem>().setResultClass(DyttTVDramaItem.class)
                        .setPageUrl(url)
                        .setJsoupEngineCallback(new JsoupEngine.JsoupEngineCallback<DyttTVDramaItem>() {
                            @Override
                            public void onJsoupParsed(List<DyttTVDramaItem> results) {
                                if (results.get(0).getPosterUrl() != null) {
                                    Picasso.with(getApplicationContext())
                                            .load(Uri.parse(results.get(0).getPosterUrl()))
                                            .into(PicassoAutoFitImageTarget.from(posterImageView));
                                }
                                setupDownloadUrls(results.get(0).getThunderUrls());
                            }
                        }).parseAsync();
            } else {
                new JsoupEngine<DyttMovieItem>().setResultClass(DyttMovieItem.class)
                        .setPageUrl(url)
                        .setJsoupEngineCallback(new JsoupEngine.JsoupEngineCallback<DyttMovieItem>() {
                            @Override
                            public void onJsoupParsed(List<DyttMovieItem> results) {
                                if (results.get(0).getPosterUrl() != null) {
                                    Picasso.with(getApplicationContext())
                                            .load(Uri.parse(results.get(0).getPosterUrl()))
                                            .into(PicassoAutoFitImageTarget.from(posterImageView));
                                }
                                if (results.get(0).getThumbnailUrl() != null) {
                                    Picasso.with(getApplicationContext())
                                            .load(Uri.parse(results.get(0).getThumbnailUrl()))
                                            .into(PicassoAutoFitImageTarget.from(thumbnailImageView));
                                }
                                setupDownloadUrls(results.get(0).getThunderUrls());
                            }
                        }).parseAsync();
            }
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Button gotoThunderButton = (Button) findViewById(R.id.dytt_detail_go_to_thunder);
        gotoThunderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.navigateToThunder(ActivityUtils.getActivity(v.getContext()), null);
            }
        });
    }

    private void setupDownloadUrls(List<String> thunderUrls) {
        DyttDownloadAdapter adapter = new DyttDownloadAdapter(thunderUrls, this);
        recyclerView.setAdapter(adapter);
    }

    public static void start(Context context, String url, int type) {
        Intent starter = new Intent(context, DyttDetailActivity.class);
        starter.putExtra(EXTRA_URL, url);
        starter.putExtra(EXTRA_TYPE, type);
        context.startActivity(starter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
