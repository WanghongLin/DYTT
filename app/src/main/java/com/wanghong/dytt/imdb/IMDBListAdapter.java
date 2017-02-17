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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wanghong.dytt.R;

import java.util.List;

/**
 * Created by wanghong on 2/17/17.
 */

public class IMDBListAdapter extends RecyclerView.Adapter<IMDBListViewHolder> {

    private List<IMDBListItem> imdbListItems;

    public IMDBListAdapter(List<IMDBListItem> imdbListItems) {
        this.imdbListItems = imdbListItems;
    }

    @Override
    public IMDBListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IMDBListViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(IMDBListViewHolder holder, int position) {
        holder.setImdbListItem(imdbListItems.get(position));
        holder.invalidate();
    }

    @Override
    public int getItemCount() {
        return imdbListItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.imdb_list_item;
    }
}
