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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wanghong.dytt.ActivityConstants;
import com.wanghong.dytt.ActivityUtils;
import com.wanghong.dytt.R;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IMDBDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String IMDB_LIST_ITEM_EXTRA = "imdb_list_item_extra";

    private ProgressBar progressBar;

    private TextView titleTextView;
    private ImageView posterImageView;
    private TextView ratedTextView;
    private TextView genreTextView;
    private TextView runtimeTextView;
    private TextView metaScoreTextView;
    private TextView imdbRatingTextView;
    private TextView imdbVotesTextView;

    private IMDBLabelTextView directorView;
    private IMDBLabelTextView actorsView;
    private IMDBLabelTextView writerView;
    private TextView plotTextView;
    private IMDBLabelTextView releaseView;
    private IMDBLabelTextView countryView;

    private IMDBListItem imdbListItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imdbdetail);

        progressBar = (ProgressBar) findViewById(R.id.imdb_detail_progressbar);

        titleTextView = (TextView) findViewById(R.id.imdb_detail_title);
        posterImageView = (ImageView) findViewById(R.id.imdb_detail_poster);
        ratedTextView = (TextView) findViewById(R.id.imdb_detail_rated);
        genreTextView = (TextView) findViewById(R.id.imdb_detail_genre);
        runtimeTextView = (TextView) findViewById(R.id.imdb_detail_runtime);
        metaScoreTextView = (TextView) findViewById(R.id.imdb_detail_metascore);
        imdbRatingTextView = (TextView) findViewById(R.id.imdb_detail_rating);
        imdbVotesTextView = (TextView) findViewById(R.id.imdb_detail_votes);
        directorView = (IMDBLabelTextView) findViewById(R.id.imdb_detail_director);
        actorsView = (IMDBLabelTextView) findViewById(R.id.imdb_detail_actors);
        writerView = (IMDBLabelTextView) findViewById(R.id.imdb_detail_writer);
        plotTextView = (TextView) findViewById(R.id.imdb_detail_plot);
        releaseView = (IMDBLabelTextView) findViewById(R.id.imdb_detail_release);
        countryView = (IMDBLabelTextView) findViewById(R.id.imdb_detail_country);
        TextView imdbComTextView = (TextView) findViewById(R.id.imdb_detail_see_more_imdb_com);
        Button imdbAppButton = (Button) findViewById(R.id.imdb_detail_see_more_imdb_app);
        Button thunderButton = (Button) findViewById(R.id.imdb_detail_go_to_thunder);
        imdbComTextView.setOnClickListener(this);
        imdbAppButton.setOnClickListener(this);
        thunderButton.setOnClickListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        try {
            getPackageManager().getPackageInfo(ActivityConstants.IMDB_PACKAGE_NAME, 0);
        } catch (PackageManager.NameNotFoundException e) {
            // e.printStackTrace();
            imdbAppButton.setVisibility(View.INVISIBLE);
        }

        imdbListItem = getIntent().getParcelableExtra(IMDB_LIST_ITEM_EXTRA);
        new IMDBDetailTask().execute(imdbListItem.getImdbID());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            if (item.getItemId() == R.id.action_share) {
                ActivityUtils.performShare(this, imdbListItem.getImdbLink());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static void start(Context context, IMDBListItem imdbListItem) {
        Intent starter = new Intent(context, IMDBDetailActivity.class);
        starter.putExtra(IMDB_LIST_ITEM_EXTRA, imdbListItem);
        context.startActivity(starter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imdb_detail_see_more_imdb_com:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.imdb.com/title/" + imdbListItem.getImdbID()));
                startActivity(intent);
                break;
            case R.id.imdb_detail_see_more_imdb_app:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("imdb:///title/" + imdbListItem.getImdbID()));
                if (i.resolveActivity(getPackageManager()) != null) {
                    Toast.makeText(this, R.string.prompt_starting_imdb, Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }
                break;
            case R.id.imdb_detail_go_to_thunder:
                BTMagnetDialog.show(this, imdbListItem.getImdbID());
                break;
            default:
                break;
        }
    }

    private class IMDBDetailTask extends AsyncTask<String, Void, IMDBDetailItem> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected IMDBDetailItem doInBackground(String... params) {
            try {
                URL url = new URL(String.format(ActivityConstants.OMDB_ENDPOINT, params[0]));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                return new Gson().fromJson(inputStreamReader, IMDBDetailItem.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(IMDBDetailItem imdbDetailItem) {
            super.onPostExecute(imdbDetailItem);
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            if (imdbDetailItem != null) {
                titleTextView.setText(imdbDetailItem.getTitle() + " (" + imdbDetailItem.getYear() + ")");
                Picasso.with(posterImageView.getContext())
                        .load(Uri.parse(imdbDetailItem.getPoster()))
                        .into(posterImageView);
                ratedTextView.setText(imdbDetailItem.getRated());
                genreTextView.setText(imdbDetailItem.getGenre());
                runtimeTextView.setText(imdbDetailItem.getRuntime());
                metaScoreTextView.setText(imdbDetailItem.getMetascore());
                imdbRatingTextView.setText(String.format("%1$s/10", imdbDetailItem.getImdbRating()));
                imdbVotesTextView.setText(imdbDetailItem.getImdbVotes());
                directorView.setText(imdbDetailItem.getDirector());
                actorsView.setText(imdbDetailItem.getActors());
                writerView.setText(imdbDetailItem.getWriter());
                plotTextView.setText(imdbDetailItem.getPlot());
                releaseView.setText(imdbDetailItem.getReleased());
                countryView.setText(imdbDetailItem.getCountry());
            }
        }
    }

}
