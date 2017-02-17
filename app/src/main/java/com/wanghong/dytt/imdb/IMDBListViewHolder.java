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

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wanghong.dytt.R;

/**
 * Created by wanghong on 2/17/17.
 */

public class IMDBListViewHolder extends RecyclerView.ViewHolder {

    private IMDBListItem imdbListItem;

    private ImageView posterImageView;
    private TextView titleTextView;
    private TextView ratingTextView;

    public IMDBListViewHolder(View itemView) {
        super(itemView);

        posterImageView = (ImageView) itemView.findViewById(R.id.imdb_item_poster);
        titleTextView = (TextView) itemView.findViewById(R.id.imdb_item_title);
        ratingTextView = (TextView) itemView.findViewById(R.id.imdb_item_rating);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMDBDetailActivity.start(v.getContext(), imdbListItem);
            }
        });
    }

    public void setImdbListItem(IMDBListItem imdbListItem) {
        this.imdbListItem = imdbListItem;
    }

    public void invalidate() {
        Picasso.with(posterImageView.getContext())
                .load(Uri.parse(imdbListItem.getPosterUrl()))
                .into(posterImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

        titleTextView.setText(imdbListItem.getTitle());
        ratingTextView.setText(imdbListItem.getRating());
    }
}
