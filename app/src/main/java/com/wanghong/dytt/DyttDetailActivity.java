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
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            if (type == TYPE_DRAMA) {
                new JsoupEngine<DyttTVDramaItem>().setResultClass(DyttTVDramaItem.class)
                        .setPageUrl(url)
                        .setJsoupEngineCallback(new JsoupEngine.JsoupEngineCallback<DyttTVDramaItem>() {
                            @Override
                            public void onJsoupParsed(List<DyttTVDramaItem> results) {
                                if (results.get(0).getPosterUrl() != null) {
                                    Picasso.with(getApplicationContext())
                                            .load(Uri.parse(results.get(0).getPosterUrl()))
                                            .into(posterImageView, PicassoAutoFitCallback.createCallback(posterImageView));
                                }
                                setupDownloadUrls(results.get(0).getThunderUrls());
                                hideProgressBar();
                            }
                        }).parseAsync();
            } else {
                new JsoupEngine<DyttMovieItem>().setResultClass(DyttMovieItem.class)
                        .setPageUrl(url)
                        .setJsoupEngineCallback(new JsoupEngine.JsoupEngineCallback<DyttMovieItem>() {
                            @Override
                            public void onJsoupParsed(List<DyttMovieItem> results) {
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
                                descriptionTextView.setText(results.get(0).getDescription());
                                Document htmlDescriptionDocument = Jsoup.parse(results.get(0).getDescription());
                                htmlDescriptionDocument.select("img").remove();
                                descriptionTextView.setText(Html.fromHtml(htmlDescriptionDocument.toString()));
                                setupDownloadUrls(results.get(0).getThunderUrls());
                                hideProgressBar();
                            }
                        }).parseAsync();
            }
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        }
        return super.onOptionsItemSelected(item);
    }
}
