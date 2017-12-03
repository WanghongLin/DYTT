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

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wanghong.dytt.model.DyttDetailViewModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class DyttDetailActivity extends AppCompatActivity {

    private static final String TAG = DyttDetailActivity.class.getSimpleName();
    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_TYPE = "extra_type";
    private static final String EXTRA_TITLE = "extra_title";

    public static final int TYPE_MOVIE = 710;
    public static final int TYPE_DRAMA = 265;

    private ImageView posterImageView;
    private ImageView thumbnailImageView;
    private TextView descriptionTextView;
    private Button gotoThunderButton;
    private ProgressBar progressBar;

    private List<String> thunderUrls;

    @Inject
    DyttDetailViewModel detailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dytt_detail);

        posterImageView = (ImageView) findViewById(R.id.dytt_detail_poster);
        thumbnailImageView = (ImageView) findViewById(R.id.dytt_detail_thumbnail);
        descriptionTextView = (TextView) findViewById(R.id.dytt_detail_description);
        TextView titleTextView = (TextView) findViewById(R.id.dytt_detail_title);
        progressBar = (ProgressBar) findViewById(R.id.dytt_detail_progressbar);

        progressBar.setVisibility(View.VISIBLE);
        String url = getIntent().getStringExtra(EXTRA_URL);
        int type = getIntent().getIntExtra(EXTRA_TYPE, TYPE_MOVIE);
        if (url != null) {
            detailViewModel.initViewModel(type, url);
            if (type == TYPE_DRAMA) {
                detailViewModel.getDyttTVDramaItems().observe(this, new Observer<List<DyttTVDramaItem>>() {
                    @Override
                    public void onChanged(@Nullable List<DyttTVDramaItem> dyttTVDramaItems) {
                        if (dyttTVDramaItems != null && dyttTVDramaItems.size() > 0) {
                            Picasso picasso = Picasso.with(getApplicationContext());
                            picasso.setLoggingEnabled(true);
                            picasso.load(Uri.parse(dyttTVDramaItems.get(0).getPosterUrl()))
                                    .into(posterImageView, PicassoAutoFitCallback.createCallback(posterImageView));
                            setupDownloadUrls(dyttTVDramaItems.get(0).getThunderUrls());
                            hideProgressBar();
                            Log.d(TAG, "onChanged: " + dyttTVDramaItems.get(0));
                        }
                    }
                });
            } else {
                detailViewModel.getDyttMovieItems().observe(this, new Observer<List<DyttMovieItem>>() {
                    @Override
                    public void onChanged(@Nullable List<DyttMovieItem> dyttMovieItems) {

                    }
                });
                new JsoupEngine<DyttMovieItem>().setResultClass(DyttMovieItem.class)
                        .setPageUrl(url)
                        .setJsoupEngineCallback(new JsoupEngine.JsoupEngineCallback<DyttMovieItem>() {
                            @Override
                            public void onJsoupParsed(List<DyttMovieItem> results) {
                                if (results == null || results.size() == 0) {
                                    return;
                                }

                                if (results.get(0).getImageUrls() != null && results.get(0).getImageUrls().size() > 0) {
                                    Picasso.with(getApplicationContext())
                                            .load(Uri.parse(results.get(0).getImageUrls().get(0)))
                                            .into(posterImageView, PicassoAutoFitCallback.createCallback(posterImageView));
                                }
                                if (results.get(0).getImageUrls() != null && results.get(0).getImageUrls().size() > 1) {
                                    Picasso.with(getApplicationContext())
                                            .load(Uri.parse(results.get(0).getImageUrls().get(1)))
                                            .into(thumbnailImageView, PicassoAutoFitCallback.createCallback(thumbnailImageView));
                                }

                                final String description = results.get(0).getDescription();
                                if (!TextUtils.isEmpty(description)) {
                                    Document htmlDescriptionDocument = Jsoup.parse(results.get(0).getDescription());
                                    htmlDescriptionDocument.select("img").remove();
                                    descriptionTextView.setText(Html.fromHtml(htmlDescriptionDocument.toString()));
                                }
                                setupDownloadUrls(results.get(0).getThunderUrls());
                                hideProgressBar();
                            }
                        }).parseAsync();
            }
        }

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            String title = getIntent().getStringExtra(EXTRA_TITLE);
            if (title != null && !TextUtils.isEmpty(title)) {
                titleTextView.setText(title);
            }
        }
        gotoThunderButton = (Button) findViewById(R.id.dytt_detail_go_to_thunder);
        gotoThunderButton.setEnabled(false);
        gotoThunderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thunderUrls != null) {
                    if (thunderUrls.size() == 1) {
                        ActivityUtils.navigateToThunder(ActivityUtils.getActivity(v.getContext()), thunderUrls.get(0));
                    } else {
                        DyttDownloadListDialog.showDialog(v.getContext(), thunderUrls);
                    }
                } else {
                    ActivityUtils.navigateToThunder(ActivityUtils.getActivity(v.getContext()), null);
                }
            }
        });
    }

    private void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setupDownloadUrls(List<String> thunderUrls) {
        this.thunderUrls = thunderUrls;
        gotoThunderButton.setEnabled(thunderUrls != null);
    }

    public static void start(Context context, String url, int type, String title) {
        Intent starter = new Intent(context, DyttDetailActivity.class);
        starter.putExtra(EXTRA_URL, url);
        starter.putExtra(EXTRA_TYPE, type);
        starter.putExtra(EXTRA_TITLE, title);
        context.startActivity(starter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            if (item.getItemId() == R.id.action_share) {
                ActivityUtils.performShare(this, thunderUrls.get(0));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
